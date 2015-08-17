# Git Crypt

## Summary
Very similar product to blackbox. Main different is its use of git filters. That means the experience is entirely in line with using git in general. On the other hand, that means you can't move out of the git world at all.

## Strengths
- Simple workflow
- Can use key pairs for encryption / decryption
- Flexible in that it isn't opinionated about automation solutions.
- Lock / unlock adds filtering to diff, so you can use tig or diff as usual

## Weaknesses
- Basically no integrations other than git.
- Very little provisioning support
- Tight coupling with git makes it so that you have to operate in a git world. For example, you can't export the repo or the unlock simply won't work.
Like blackbox its lack of opinion about application space means it doesn't encourage good behavior outside the SCM space.

## Ratings (poor / fair / good)
- Ease of setup: fair
- Easy of use: fair
- Cloud readiness: good
- Data center readiness: good
- Automation / pipeline readiness: fair
- Product maturity: fair
- Developer friendliness: good
- Documentation: fair
- Stability: good
- Auditability: poor (nothing for the app. commit-level tracking, but that's not really reliable) 

## Features
- Easy support for symmetric keys (feature? hmmm)

## How does it attempt to solve the bootstrapping problem?
Barely does.

## Notes
- No good package for unix. Old school build and deploy.
- Don't forget to add a user or you won't be able to decrypt
