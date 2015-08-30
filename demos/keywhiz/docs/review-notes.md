# Square Keywhiz
Square's keywhiz is another secrets-as-a-service in the spirit of vault. It has a JSON-based API, but adds another interesting feature, which is a transparent FUSE-based file system so the application can transparently use a secret store as if it was a mounted file system.

## Summary
Not ready for prime time. Not a very interesting clustering story. Docs are sparse. Community is very thin and unlike Hashicorp's devs, the team doesn't seem to be as engaged in it.

## Strengths
Hard to tell.

## Weaknesses
- java-based CLI is very slow because of JDK spin-up
- no obvious HA strategy - simply stateless with database integration... might work
- docs for API are very, very rough. In fact, mostly non-existent.

## Ratings (poor / fair / good)
- Ease of setup: poor
- Easy of use: poor
- Cloud readiness: fair
- Data center readiness: fair
- Automation / pipeline readiness:
- Product maturity: poor
- Developer friendliness: poor
- Documentation: poor
- Stability: ???
- Auditability: ???

## Features
- FUSE file system
- REST API
- Secret versioning

## How does it attempt to solve the bootstrapping problem?

## Notes
- There seems to be less activity on the mailing list for keywhiz than vault
- How does it store things, and how does it encrypt / decrypt?
