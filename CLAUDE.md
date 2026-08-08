# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository. It is written to be portable — copy this file (with `Configuration`/project-specific sections stripped or replaced) into any Spring Boot + Maven project built with the same hexagonal-architecture conventions and it should still apply.

## Commands

```bash
# Build
./mvnw clean package

# Run all tests
./mvnw test

# Run a single test class
./mvnw test -Dtest=<Feature>FacadeTests

# Run a single test method
./mvnw test -Dtest=<Feature>FacadeTests#shouldCreateSomethingWithGeneratedId

# Run mutation testing (see "Mutation Testing" below)
./mvnw clean test-compile org.pitest:pitest-maven:mutationCoverage
```

## Architecture

The app follows **Hexagonal Architecture (Ports & Adapters)**. Every feature follows this exact shape:

```
<Feature>Controller → <Feature>Facade → <Feature>Service → <Feature>Port (interface) → Jpa<Feature>Adapter
```

- **`<Feature>Controller`** — REST layer, maps HTTP to facade calls
- **`<Feature>Facade`** — orchestrates ID generation, mapping, and service calls
- **`<Feature>Service`** — contains all domain/business logic
- **`<Feature>Port`** — the port interface; the only contract the service layer depends on
- **`Jpa<Feature>Adapter`** — the JPA implementation of the port, maps between domain models and entities

### Naming — identical method names across Controller/Facade/Service

`<Feature>Controller.fetchAllByX` → `<Feature>Facade.fetchAllByX` → `<Feature>Service.fetchAllByX` — same name at every layer, always. Same for `fetchById`, `create`, `update`, `delete`. Sub-resources (e.g. a parent's child collection) get their own `createX`/`updateX`/`deleteX` trio named after the entity, not generic verbs like `add`. This is a hard rule — a Controller/Facade/Service with mismatched names is a bug to fix, not a style nit: it makes the codebase impossible to navigate confidently ("if I know the Facade method name, I know the Controller and Service method names too, no need to double check").

The **Port/Adapter** layer breaks from this and uses persistence-style vocabulary instead (`save`/`update`/`delete`), matching what the ORM/data layer actually does — e.g. `<Feature>Service.createChild` calls `<Feature>Port.saveChild`, not `createChild`. Controller/Facade/Service speak REST/domain language; Port/Adapter speak persistence language. Don't force one vocabulary across all five layers — each layer names things after what it actually is.

## Domain Models vs Persistence Entities

Domain models are plain data objects (Java records), living separately from persistence entities (JPA `@Entity` classes, or equivalent). The Adapter is the *only* place that converts between them. Never let a persistence entity leak into the Service/Facade/Controller layers, and never let a domain model carry persistence annotations.

## Domain Logic Ownership

All business decisions (what changes, what resets, what's rejected) are computed **once**, in the domain model, exposed via patch/mutation methods on the domain object itself (e.g. `SomeDetail.applyPatch(fieldA, fieldB, ...)`, overloaded per use case rather than one giant method trying to handle every combination). The Service's job is: `current = port.fetchById(id)` → `updated = current.applyPatch(...)` → `port.update(updated)` — it never re-derives the decision itself, and the Adapter never re-implements the business rule either. The Adapter is a dumb state-syncer: it persists whatever the passed domain object says (relying on ORM dirty-checking so unchanged fields produce no writes). If you find the same business rule implemented independently in two places (domain object and adapter, or domain object and service), that's the bug pattern to fix — pick one owner and make the other one trust it.

## Repository Pattern

Each repository has three parts:
1. A domain interface (e.g., `ThingRepository`) declaring the needed methods, framework-agnostic
2. A framework-specific interface (e.g., `JpaThingRepository`) implementing/extending both the ORM's base repository type and the domain interface
3. A `Fake*` implementation (e.g., `FakeThingRepository`) used in tests — records the arguments it was called with as public fields, for assertion

Prefer methods the ORM can derive natively from the method name (`findByOwnerId`, `findByOwnerIdAndActiveTrue`, `deleteByParentIdAndChildId`, ...) over hand-written queries. Only hand-write a query when native derivation genuinely can't do it — the recurring legitimate case is graph/join-fetch loading (see "Entity Graph Loading" below).

## Cross-Feature Reads

A feature may depend directly on another feature's repository interfaces or persistence entities for **read-only** composition, bypassing that other feature's Port/Service/domain layer entirely, when going through the full stack would add pure indirection with no behavioral benefit. This is intentional, not a hexagonal-architecture violation to "fix" — prefer it over inventing a "lookup" role-interface just to avoid a direct dependency, when a direct one is simpler and equally clear. Reserve the full Port/Service path for anything that involves business logic, not plain projection/aggregation of already-decided data.

## Entity Graph Loading

When a persistence entity needs both (a) a zero-fetch write path and (b) a fully-loaded read path for the same relation, map the same DB column twice: a plain writable field (e.g. `String parentId`) for inserts/updates, plus a read-only lazy relation (e.g. JPA `@ManyToOne(fetch = LAZY) @JoinColumn(..., insertable = false, updatable = false)`) for reads. Pair this with a join-fetch query override on the specific repository method that needs the full graph, so reads are a single round-trip. Don't apply this globally — only override the specific method(s) that actually need the graph; leave simpler list/lookup queries as plain lazy relations so they stay cheap.

## Endpoint Minimalism

Don't add a write endpoint that bundles multiple writes (e.g. create-and-attach-in-one-call) when the caller can sequence two existing calls instead — that's usually pushing backend complexity (transaction boundaries spanning multiple repositories) onto the server to save the caller a small amount of sequencing/UX work it's equally capable of owning. Don't add a read endpoint that's just a filtered/reshaped view of what an existing endpoint already returns — let the caller filter client-side. Only build a new dedicated endpoint when there's a real reason an existing one can't serve the need (e.g. it aggregates multiple distinct data sources with logic — grouping, deduplication — that no existing endpoint could produce, or there's a genuine atomicity requirement).

## Testing Approach

Tests use hand-written fakes — no mocking framework. `Fake<Feature>Adapter` implements the Port and records method arguments for assertion. `Fake*Repository` classes do the same at the persistence layer. All tests follow Given/When/Then comments. Only two test classes per feature:

- `<Feature>FacadeTests` — tests the full facade→service→port flow using `Fake<Feature>Adapter`
- `Jpa<Feature>AdapterTests` — tests the adapter's mapping using `Fake*Repository` implementations

No separate Service-level test files — business logic is exercised through `*FacadeTests`, since the Service has no observable behavior independent of the Facade calling it.

Additional hygiene, learned from actually running mutation testing against this style of codebase:
- Use **distinguishable values** for sibling boolean/field pairs in test fixtures (e.g. `flagA=true, flagB=false`, never both the same) — identical values hide field-swap bugs from both manual review and mutation testing, since a mutant that swaps two fields produces the same observable result when both fixture values happen to match.
- For every patch-style business rule, test both directions (rule triggers / rule doesn't) **and** the case where the rule must override an explicitly conflicting input in the same request (e.g. a "mark active" flag forcing a "completed" flag back to false, even when the caller also explicitly passed `completed=true` in the same call).
- When a Service method's only observable effect is a port call (not its return value), assert the Fake's recorded interaction, not just the response — otherwise a mutant/bug that silently removes the port call can't be caught by the test at all.
- `orElseThrow(() -> new RuntimeException("..."))`: `assertThrows(RuntimeException.class, ...)` alone doesn't verify the *right* exception was thrown — a supplier that's mutated/buggy into returning `null` throws a `NullPointerException` instead, which is also a `RuntimeException` and silently passes the same assertion. Always assert on `exception.getMessage()` too.

## Mutation Testing

`pitest-maven` set up in `pom.xml`, not bound to any lifecycle phase (never runs as part of `mvn test`, only when explicitly invoked):
```bash
./mvnw clean test-compile org.pitest:pitest-maven:mutationCoverage
```
`test-compile` must be explicit — invoking the `mutationCoverage` goal alone does not recompile test sources first, so it can silently run against stale test bytecode after an edit. Report: `target/pit-reports/index.html`.

Scope `targetClasses`/`targetTests` to your own base package, and use `excludedClasses` to cut out anything with no meaningful unit-test target: persistence entities, request/response DTOs, `*Controller` (tested indirectly via the Facade, not directly), `*Config` (pure wiring), plain exception classes, repository interfaces (no logic of their own beyond derived queries), and `Fake*` test doubles. Without this, the score gets diluted by classes nothing was ever meant to unit-test directly, which looks like a real gap but isn't. Target: 100% killed on the remaining, actually-tested classes — treat any survivor as either a real test gap (apply the patterns above) or a legitimate scope adjustment, never as "good enough."

If mutation testing fails immediately with an `Unsupported class file major version` error, the bytecode target (project's Java version) is newer than what the installed `pitest-maven`/ASM can read — check for a newer `pitest-maven` release before assuming the toolchain simply doesn't support it yet; the ecosystem usually catches up faster than expected.
