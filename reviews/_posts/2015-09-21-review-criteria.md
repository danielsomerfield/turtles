---
layout: post
title: Tool Review Criteria
category: review
---
During the process of preparing for this presentation, I did a survey of a number of products to help implement a secret storage strategy. I did not approach these from a security analysts perspective but more from the devops side.

The criteria I used are as follows:

- Ease of setup - is the product straight-forward to deploy and do initial configuration?
- Easy of use - once set up, is the product intuitive? do you need to spend a lot of time going to the docs to figure out how to use it?
- Cloud readiness - does the product qualify as "cloud native": easily deployable, stateless, loosely couple dependencies, etc...?
- Datacenter readiness - will the product function easily in a more traditional corporate datacenter?
- Automation / pipeline readiness - does the product integrate well with continuous integration and continuous delivery environments or is it likely to be come the manual bottleneck?
- Product maturity - has the product been around and gone through some iterations so the wrinkles have been ironed out?
- Developer friendliness - doe the APIs and/or command-line parameters are straightforward and the product has a good story for integrating into existing code?
- Documentation - RTFM
- Stability - does it crash or corrupt data?
- Auditability - does it provide decent auditing for both change and access or are you forced to build a layer on top?

Admittedly, some criteria proved to be more meaningful than others. For example, the "data center readiness" attribute ended up pretty flimsy. Any product I looked at that wasn't "data center ready" wasn't "cloud ready" either, due to product maturity.

Should I start reviewing any SAAS-type products, I think that will change so I'll leave the criteria up there.  
