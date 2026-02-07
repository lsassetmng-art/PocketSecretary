# PocketSecretary — Canonical MVP

## Status
- **Phase**: G-1 (presence-only)
- **Branch**: master (canonical)
- **Policy**: additive features are intentionally deferred

## Design Principles
- **Silence-first**: no sound, no animation, no events
- **Presence-only**: existence without interference
- **Do not escalate**: if silence is better, keep silence
- **Human calm > feature count**

## Safety Declaration
This repository is a **safe folder**.

### Explicitly NOT included
- Secrets / API keys
- Shell profiles (.bashrc, .zshrc)
- SDKs, caches, personal storage
- Parent directories or sibling projects

### Enforcement
- `.gitignore` blocks sensitive paths
- Canonical history contains **no secrets**
- `master` is the single source of truth

## Governance
- Breaking this design requires **STOP**
- Any addition must prove it reduces anxiety
- If unsure: **do nothing**

— Canonical MVP
