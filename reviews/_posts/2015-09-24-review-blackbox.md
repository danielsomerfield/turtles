---
layout: post
title: Blackbox
category: review
---
## Review tl;dr
Decent command-line tool for distributing secrets via SCM. Useable. No frills. Command line tools are layered on top, so you need to use the blackbox_* tools instead of your native SCM CLI. It offers GPG sub-keys as a mechanism for allowing access from automated users.

## Product Summary
Blackbox is for storing secrets in Git, Mercurial, Subversion or Perforce. It was originally built for Puppet but that is no longer required. The basic model is that a series of scripts mediate the storage, encryption, and decryption of files. Keys are stored encrypted in SCM such that all authorized users can access them. Encryption is done via GPG.

You would generally not use blackbox as a service of any kind, nor would you be likely to use it during application runtime, but for development of manual secret access, and at provisioning time with your orchestration server.

It's worth noting that unlike most similar tools, blackbox has a story for automated users using sub-keys. It's not incredibly sophisticated, but similar tools don't even have a story for this.

It's worth noting that I did not evaluate the puppet use case, but just the tool as a command line tool. If you are in a puppet environment, you will probably want to do a separate evaluation of the integration.

## Evaluation

### Strengths
- Can use key pairs for encryption / decryption
- Strong puppet support
- Flexible in that it isn't opinionated about automation solutions.
- Good support for multiple repo types.
- Large number of SCM integrations

### Weaknesses
- Doesn't integrate terribly well with git tools out of the box. (like tig, git history, etc).
- Only supports Centos/RedHat, OS-X, and Cygwin.
- Doesn't discourage storage of secrets on the machines--leaves it up to the dev.
- Automated deploy requires a build of an RPM, or copying source. Not in repos.
- Chef deploy encourages decryption at the puppet master, rather than the node
- No API outside of command line (and Puppet)

### Ratings
- Ease of setup: fair *(originally rated this as "poor", but compared to group at large, it was pretty straight-forward, so I'll call it "fair" if graded on the curve)*
- Easy of use: good
- Cloud readiness: fair
- Data center readiness: fair
- Automation / pipeline readiness: poor
- Product maturity: good
- Developer friendliness: fair
- Documentation: fair
- Stability: good
- Auditability: poor. Doesn't stop you, but doesn't help you much either.
