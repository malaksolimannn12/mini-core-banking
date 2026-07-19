package com.bank.minicorebanking.entity;

public enum Role {
    ADMIN,
    CUSTOMER
}
//Good — quick check on this one.
//
//## What you said
//
//"Enum defines the role, must be USER or ADMIN, can't have any other enrollment."
//
//## Confirming — mostly right, one small correction
//
//✅ Correct: an enum restricts a field to a **fixed, predefined set of values** — nothing outside that list is allowed.
//
//⚠️ Small correction: your actual enum values are `ADMIN` and `CUSTOMER`, not `ADMIN` and `USER`:
//
//```java
//public enum Role {
//    ADMIN,
//    CUSTOMER
//}
//```
//
//Just a naming mix-up, not a conceptual error — the *word* "user" is naturally confusing here since we also have a `User` **entity** (the login account itself) that *contains* a `role` field, and that `role` field can only be `ADMIN` or `CUSTOMER`. So:
//
//- `User` = the entity (a login account: username, password, role)
//- `Role` = the enum (the two allowed values *for* that account's role field: `ADMIN` or `CUSTOMER`)
//
//Also worth restating precisely: "enrollment" isn't quite the right word here — the correct term is just **"value"**. An enum has a fixed list of allowed *values*.
//
//Good grasp of the concept overall, just tighten the terminology (`CUSTOMER` not `USER`, and "value" not "enrollment"). Ready to move to the `User` entity's fields next?
