# AI Tools Usage

## Tools Used

### 1. Claude Code (Anthropic)

**Purpose**: Primary development assistant

**How it helped**:

- Architecture design and Clean Architecture implementation
- Kotlin/Jetpack Compose code generation
- Feature module structure (`feature:search`)
- Code review and refactoring suggestions
- Error handling patterns (SharedFlow for one-time events)
- Documentation updates

### 2. Figma

**Purpose**: UI/UX design

**How it helped**:

- Designed app screens before implementation
- Created visual references stored in `/goal` folder
- Established consistent design language (colors, spacing, typography)

### 3. RapidAPI

**Purpose**: API exploration and validation

**How it helped**:

- Tested OpenWeatherMap API endpoints
- Validated JSON response structures
- Understood data relationships for model design

## Development Workflow

```text
1. RapidAPI     → Validate API, understand data structure
2. Figma        → Design UI mockups, save to /goal folder
3. Claude Code  → Implement features with Clean Architecture
```
