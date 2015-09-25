---
layout: review
title: Git-Crypt
category: review
---
## Review tl;dr
Viable command line tool for encrypting items in git repos. A little more straightforward to use than some of the others because git filters means you get a native "git experience".

## Product Summary
Another command line tools for encrypting data in a git repo using filters. Encryption is done via GPG. According to the site, repos that are public or mixed secret and non secret content are the sweet spot for git-crypt:

*As such, git-crypt is not the best tool for encrypting most or all of the files in a repository. Where git-crypt really shines is where most of your repository is public, but you have a few files (perhaps private keys named *.key, or a file with API credentials) which you need to encrypt"*

## Evaluation

### Strengths
- Simple workflow
- Can use key pairs for encryption / decryption
- Lock / unlock adds filtering to diff, so you can use tig or git diff diff as usual

### Weaknesses
- Basically no integrations other than git.
- Very little provisioning support
- Tight coupling with git makes it so that you have to operate in a git world. For example, you can't export the repo or the unlock simply won't work.

### Ratings
- Ease of setup: poor
- Easy of use: fair
- Cloud readiness: good
- Datacenter readiness: good
- Automation / pipeline readiness: fair
- Product maturity: fair
- Developer friendliness: good
- Documentation: fair
- Stability: good
- Auditability: poor
