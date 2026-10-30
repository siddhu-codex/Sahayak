# Sahayak AI — Modern Cobalt & Navy Layout Redesign

Keep 100% of the original **Marathi-First AI, Voice Technology, Live Translation, Citizen Schemes, Mandi Rates, and Document Reader** core features, while completely transforming the visual interface to match the layout, card styling, and color palette (clean white, deep royal navy, and electric cobalt blue) shown in the reference image, with **English as the default interface language**.

---

### User Review & Critical Decisions

> [!IMPORTANT]
> - **Core App Features Preserved**: The entire engine (Dual-Brain Marathi Voice Concierge, Multi-Dialect ASR/TTS, Live Speech Bhashantar, MahaDBT/Ladki Bahin schemes, Agri mandi rates, Document OCR, and multi-turn voice forms) remains completely intact.
> - **Design Transformation**: The UI layout is revamped to match the reference design:
>   - **Color System**: Clean white canvas (`#FFFFFF` / `#F8FAFC`), deep royal navy cards (`#0F172A`, `#1E293B`), electric cobalt blue action buttons & active indicators (`#2563EB`, `#3B82F6`), and soft ice blue container tints (`#EFF6FF`, `#DBEAFE`).
>   - **Default UI Language**: English is the default interface language (e.g., *Dashboard*, *Live Interpreter*, *Govt Schemes*, *Mandi Rates*, *Document OCR*, *Voice Assistant*), while the speech engine and translation layer continue to handle Marathi and regional dialects.
>   - **Layout Patterns**: Metric cards at the top, featured dark navy action hero cards, clean white list cards with visual tags, and a modern bottom navigation bar.

---

### 1. Overview & Core Concept

- **What It Does**: Sahayak AI with a modern, high-performance UI inspired by modern dashboard aesthetics. Users navigate through an English-first interface while speaking or interacting in regional Marathi dialects (Varhadi, Ahirani, Malvani, Pramaan, Minglish) or translating live speech to English/Hindi.
- **Key Value**: Delivers the visual elegance and speed of modern fitness/productivity dashboards to public digital access, government welfare schemes, and agricultural intelligence.

---

### 2. User Experience & Visual Design (Inspired by Reference)

#### Color System & Tokens
- **Background**: Crisp Off-White (`#F8FAFC`), Pure Card White (`#FFFFFF`)
- **Primary Accent**: Electric Cobalt Blue (`#2563EB`, `#1D4ED8`)
- **Card Highlights**: Soft Ice Blue (`#EFF6FF`, `#DBEAFE`)
- **Hero & Dark Surfaces**: Midnight Royal Navy (`#0F172A`, `#1E293B`, `#172554`)
- **Text & Hierarchy**: Slate Dark (`#0F172A`), Slate Muted (`#64748B`), Pure White on dark cards (`#FFFFFF`)

#### Screen Redesigns
1. **Dashboard (Main Hub)**:
   - **Top Telemetry Row**: 3 minimal metric cards matching the reference header (e.g., "5 Active Schemes", "4 Mandi Markets", "1.5s Voice Latency").
   - **Featured Dark Navy Card**: "Voice Concierge & Live Interpreter" with cobalt gradient accent, active audio waveform indicator, and high-contrast "START VOICE" button.
   - **Quick Access Categories**: Clean horizontal pill chips (All, Schemes, Agri & Mandi, Live Interpreter, Document OCR).
   - **Service Cards**: Sleek white cards with ice-blue category tags, audio playback icons, and quick-action buttons.
2. **Live Speech Interpreter ("Thhet Bhashantar")**:
   - Dual-card split-screen conversational view in deep royal navy and crisp white.
   - Live Devanagari captions and instant English translation cards with speaker badges and volume playback.
   - One-tap quick conversational prompt pills.
3. **Govt Schemes & Welfare Hub**:
   - Clean search bar with ice-blue focus border.
   - Category filter pills (Women & Child, Farmers, Healthcare, Seniors, Students).
   - Benefit cards with clear metric badges (e.g. ₹1,500/month, ₹5 Lakh medical coverage) and voice application button.
4. **MahaAgri Market & Crop Doctor**:
   - APMC Mandi rates table with price trend badges (+₹75, +₹120) and modal rates in cobalt blue.
   - Crop Doctor pest alerts (Yellow Mosaic, Pink Bollworm) with expert treatment remedies.
5. **Document OCR Reader**:
   - Dark navy camera scan viewfinder matching Screen 3 of the reference image.
   - Simple Marathi audio explanation and verified key-fields extraction (7/12 Satbara, Ration card, Pension orders).
6. **Global Interactive Mic Overlay**:
   - 3-mode selector (Tap to Speak, Hold to Talk in noisy markets, Live Translate).
   - Cobalt blue and ice-blue animated waveform pulse.

---

### 3. Key Technical Decisions

- **English UI Labels with Full Marathi Audio**:
  - UI labels, headings, and navigation are presented in crisp English (*"Dashboard"*, *"Govt Schemes"*, *"Agri Mandi"*, *"Live Interpreter"*, *"Document OCR"*, *"Voice Concierge"*), eliminating translation clutter while preserving Devanagari audio synthesis and Marathi dialect processing.
- **High-Speed Compose Layouts**:
  - Eliminates heavy nested hierarchies; uses lightweight Cards with exact padding, fast state hoisting, and instant bottom navigation switching.
- **Theme Color Realignment**:
  - Updates `Color.kt` and `Theme.kt` to the clean white, royal navy, and electric cobalt blue palette shown in the reference image.

---

### 4. System Architecture Diagram

```
┌──────────────────────────────────────────────────────────────┐
│             MainActivity (English UI + Insets)               │
└──────────────────────────────┬───────────────────────────────┘
                               │
       ┌───────────────────────┴───────────────────────┐
       ▼                                               ▼
┌──────────────┐                             ┌──────────────────┐
│ Modern Navy  │ (Brand, Dialect Chip,       │ MainViewModel    │
│ Top App Bar  │  Screen Reader, Audio Pill) │ (Fast StateFlow) │
└──────────────┘                             └─────────┬────────┘
       │                                               │
       ▼                                               ▼
┌──────────────────────────────────────────────────────────────┐
│                    Redesigned Active View                    │
│  ├── DashboardView (Metric cards, dark navy hero, services)  │
│  ├── SchemesView (Clean search, filter pills, benefit cards) │
│  ├── AgriMandiView (Mandi rates, price trends, pest doctor)  │
│  ├── LiveTranslateView (Split-screen dual cards, audio play) │
│  └── DocumentScannerView (Dark navy viewfinder, audio summary│
└──────────────────────────────┬───────────────────────────────┘
                               │
       ┌───────────────────────┴───────────────────────┐
       ▼                                               ▼
┌──────────────────────────────┐             ┌──────────────────┐
│ Modern Bottom Navigation Bar │             │ Voice & Domain   │
│ (Dashboard, Schemes, Center  │             │ Engine (Gemini   │
│  Cobalt Mic, Agri, Translate,│             │  RAG + TTS +     │
│  Docs)                       │             │  Dialect Engine) │
└──────────────────────────────┘             └──────────────────┘
```

---

### 5. Verification Plan

1. **Compilation**: Run `compile_applet` to verify clean build.
2. **Visual Inspection**: Ensure all screens feature the crisp white, midnight navy, and electric cobalt blue styling from the reference image with English default UI text.
3. **Core Functions**: Verify that Voice Concierge, Live Translation, Document OCR, and Scheme inquiries continue to function with audio speech and dialect processing.
