---
layout: review
title: transcrypt
category: review
---
## Review tl;dr
Minimal dependencies, so easy to install and easy to use. Nice integration with git command line, but doesn't provide a great deal of functionality. Only supports shared secrets which is very limited as far as controlling access.

## Product Summary
transcrypt is another in a relatively long of tools that focus on encrypting secrets in SCM. transcrypt is unique in that it uses openssl rather than gpg, like most similar tools.

## Evaluation
It does what it does well, it just doesn't do very much. Pretty easy to install: it's available via brew, and since there are so few dependencies it's pretty easy to install manually. Requires a bit of manual-hacking of the git repo to add a file, but still pretty straightforward to use. Biggest liability is the lack of support for asymmetric encryption, so you can't revoke access for individual users.

### Strengths
- very few dependencies
- .gitattributes modifications means that tig or git diff work as usual

### Weaknesses
- No support for asymmetric encryption
- Only supports git

### Ratings
- Ease of setup: good
- Easy of use: good
- Cloud readiness: good
- Datacenter readiness: good
- Automation / pipeline readiness: good
- Product maturity: fair
- Developer friendliness: good
- Documentation: fair
- Stability: good
- Auditability: poor
