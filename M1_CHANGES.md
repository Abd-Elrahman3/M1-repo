# M1 implementation report

M1 Domain core is implemented according to `docs/SHARED_MILESTONES.md` and
README sections 4.1, 4.2, 4.7, BR-101 and BR-102.

All paths below are relative to the `substream/` project root.

## Production changes

| File | Location | What changed |
| --- | --- | --- |
| `src/main/java/com/substream/domain/SubscriptionPlan.java` | Lines 11–22 | Added private final tier, monthly price and user limit fields, with a constructor. FREE = tier 1 / 0.00 / 1 user; PRO = tier 2 / 29.00 / 10 users; ENTERPRISE = tier 3 / 199.00 / Integer.MAX_VALUE users. Prices use exact BigDecimal string values. |
| `src/main/java/com/substream/domain/SubscriptionPlan.java` | Lines 25–45 | Implemented both getters, unlimited-plan detection, inclusive seat limits starting at 1, and tier-based upgrade comparison. Tier remains internal, without a getter. |
| `src/main/java/com/substream/domain/EmailValidator.java` | Lines 12–27 | Implemented BR-102 using strings and character checks: reject null, trim first, reject remaining whitespace, require exactly one @ with a nonempty local part, and require a dot after @. Added no extra email-format restrictions. |
| `src/main/java/com/substream/domain/Customer.java` | Lines 16–19, 25–38 | Added four private final fields. The constructor checks the trimmed name for blankness and the 100-character limit, delegates email validation to EmailValidator, and raises InvalidCustomerDataException for invalid names or emails. It stores the trimmed name and a trimmed email lowercased with Locale.ROOT. ID and creation date are stored as supplied. |
| `src/main/java/com/substream/domain/Customer.java` | Lines 41–70 | Implemented all four getters and ID-only equals/hashCode, following the pattern already supplied for Invoice and Subscription. |

## Added tests

All three classes are under `src/test/java/com/substream/domain/`.

| File | Cases covered | Executed tests |
| --- | --- | ---: |
| `CustomerTest.java` | Null/blank names; 1, 99, 100, 101 and 102 characters after trimming; preservation of inner spaces; email normalization; exception types; minimal valid email; equality, hash code and set behavior. | 25 |
| `EmailValidatorTest.java` | Each BR-102 condition, null/empty input, trimming, whitespace, multiple @ characters, domain dots, and deliberately permissive valid addresses. | 27 |
| `SubscriptionPlanBoundaryTest.java` | All plan prices with scale 2, seat boundaries including integer extremes, and all nine upgrade combinations. | 25 |

## Validation

Ran with Eclipse Temurin JDK 17.0.21 and Apache Maven 3.9.11:

```bash
mvn -Dtest=M1StructureTest,SubscriptionPlanTest,CustomerTest,EmailValidatorTest,SubscriptionPlanBoundaryTest clean test
```

Result: **BUILD SUCCESS — 96 tests, 0 failures, 0 errors, 0 skipped.**

- Provided `M1StructureTest`: 3 tests passed.
- Provided `SubscriptionPlanTest`, including nested tests: 16 tests passed.
- New M1 tests: 77 tests passed.
- Maven compiled all 46 production source files and all 11 test source files,
  targeting Java 17.

The run selects M1 tests. M2 and M3 structure tests belong to later, unfinished
milestones; the starter guide explains that they remain red until those phases
are implemented.

The original ZIP was used as the byte-for-byte baseline. Only the three M1
production files were modified; three test files and this report were added.
The provided tests, protected files, Maven configuration, CI configuration, and
later-phase code retain their uploaded contents.

## Package contents

`substream-m1.zip` contains the project source, documentation, tests and this
report. Git metadata, IntelliJ metadata, build output and temporary tooling are
excluded. No remote repository actions were performed.

To apply the work to an existing checkout, copy the three production files and
three new test files listed above into the same relative paths, then run the
M1 validation command.
