# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Commands

```bash
# Build
./mvnw clean package

# Run (requires SUPABASE_PASSWORD env var)
SUPABASE_PASSWORD=<password> ./mvnw spring-boot:run

# Run all tests
./mvnw test

# Run a single test class
./mvnw test -Dtest=RoomFacadeTests

# Run a single test method
./mvnw test -Dtest=RoomFacadeTests#shouldCreateRoomWithGeneratedId
```

## Architecture

The app follows **Hexagonal Architecture (Ports & Adapters)**. The single domain is `room`, organized as:

```
RoomController → RoomFacade → RoomService → RoomPort (interface) → JpaRoomAdapter
```

- **`RoomController`** — REST layer, maps HTTP to facade calls
- **`RoomFacade`** — orchestrates UUID generation, mapping, and service calls
- **`RoomService`** — contains domain logic (e.g., filtering archived vs active expenses)
- **`RoomPort`** — the port interface; the only contract the service layer depends on
- **`JpaRoomAdapter`** — the JPA implementation of `RoomPort`, maps between domain models and entities

## Domain Models vs JPA Entities

Domain models (`Room`, `Payer`, `Expense`) are **Java records** in `room/models/`. They are completely separate from JPA entities (`RoomEntity`, `PayerEntity`, `ExpenseEntity`, `ExcludedPayerEntity`) in `room/models/entities/`. `JpaRoomAdapter` handles all conversion between them.

## Expense Archiving

`deleteAllExpenses` does **not** delete — it archives (sets `archived = true`). The `GET /rooms/{id}` endpoint strips archived expenses; `GET /rooms/{id}/history` shows only archived ones. This logic lives in `RoomService` and domain model methods (`deleteArchivedExpenses`, `deleteNotArchivedExpenses`).

## Repository Pattern

Each repository has three parts:
1. A domain interface (e.g., `ExpenseRepository`) declaring the needed methods
2. A JPA interface (e.g., `JpaExpenseRepository`) extending both Spring Data `Repository<T, ID>` and the domain interface
3. A `Fake*` implementation (e.g., `FakeExpenseRepository`) used in tests

## Testing Approach

Tests use hand-written fakes — no Mockito or mocking framework. `FakeRoomAdapter` implements `RoomPort` and records method arguments for assertion. `Fake*Repository` classes do the same at the persistence layer. All tests follow Given/When/Then comments.

- `RoomFacadeTests` — tests the full facade→service→port flow using `FakeRoomAdapter`
- `JpaRoomAdapterTests` — tests the adapter mapping using `Fake*Repository` implementations

## Configuration

- **Database**: PostgreSQL via Supabase; requires `SUPABASE_PASSWORD` environment variable. Connection uses a session pooler with HikariCP (max 5 connections).
- **Rate limiting**: 100 requests/minute per IP via Bucket4j (`RateLimitConfig`).
- **CORS**: Allows `http://localhost:4200` and `https://delaudes.github.io`.
- **UUID generation**: `UuidGenerator` interface with `JavaUuidGenerator` (prod, wired via `UuidConfig`) and `FakeUuidGenerator` (tests).
- **Bean wiring**: `RoomFacade`, `RoomService`, and `RoomMapper` are not `@Component`-annotated — they are instantiated explicitly in `RoomConfig`.
