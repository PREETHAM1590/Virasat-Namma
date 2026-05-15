---
name: Virasat
colors:
  surface: '#f9faf5'
  surface-dim: '#d9dad6'
  surface-bright: '#f9faf5'
  surface-container-lowest: '#ffffff'
  surface-container-low: '#f3f4ef'
  surface-container: '#edeee9'
  surface-container-high: '#e7e9e4'
  surface-container-highest: '#e2e3de'
  on-surface: '#1a1c19'
  on-surface-variant: '#454836'
  inverse-surface: '#2e312e'
  inverse-on-surface: '#f0f1ec'
  outline: '#767964'
  outline-variant: '#c6c8b0'
  surface-tint: '#556500'
  primary: '#556500'
  on-primary: '#ffffff'
  primary-container: '#b7d23f'
  on-primary-container: '#4a5800'
  inverse-primary: '#b7d23f'
  secondary: '#4c644f'
  on-secondary: '#ffffff'
  secondary-container: '#ceeace'
  on-secondary-container: '#526a54'
  tertiary: '#556157'
  on-tertiary: '#ffffff'
  tertiary-container: '#bdcabd'
  on-tertiary-container: '#4a554b'
  error: '#ba1a1a'
  on-error: '#ffffff'
  error-container: '#ffdad6'
  on-error-container: '#93000a'
  primary-fixed: '#d3ef59'
  primary-fixed-dim: '#b7d23f'
  on-primary-fixed: '#181e00'
  on-primary-fixed-variant: '#3f4c00'
  secondary-fixed: '#ceeace'
  secondary-fixed-dim: '#b2ceb3'
  on-secondary-fixed: '#09200f'
  on-secondary-fixed-variant: '#354c38'
  tertiary-fixed: '#d9e6d8'
  tertiary-fixed-dim: '#bdcabd'
  on-tertiary-fixed: '#131e16'
  on-tertiary-fixed-variant: '#3e4a40'
  background: '#f9faf5'
  on-background: '#1a1c19'
  surface-variant: '#e2e3de'
typography:
  display-lg:
    fontFamily: Plus Jakarta Sans
    fontSize: 48px
    fontWeight: '700'
    lineHeight: 56px
    letterSpacing: -0.02em
  headline-lg:
    fontFamily: Plus Jakarta Sans
    fontSize: 32px
    fontWeight: '600'
    lineHeight: 40px
    letterSpacing: -0.01em
  headline-md:
    fontFamily: Plus Jakarta Sans
    fontSize: 24px
    fontWeight: '600'
    lineHeight: 32px
  body-lg:
    fontFamily: Be Vietnam Pro
    fontSize: 18px
    fontWeight: '400'
    lineHeight: 28px
  body-md:
    fontFamily: Be Vietnam Pro
    fontSize: 16px
    fontWeight: '400'
    lineHeight: 24px
  label-md:
    fontFamily: Be Vietnam Pro
    fontSize: 14px
    fontWeight: '600'
    lineHeight: 20px
    letterSpacing: 0.05em
rounded:
  sm: 0.5rem
  DEFAULT: 1rem
  md: 1.5rem
  lg: 2rem
  xl: 3rem
  full: 9999px
spacing:
  unit: 8px
  container-padding: 24px
  element-gap: 16px
  overlap-margin: -32px
  section-margin: 48px
---

## Brand & Style
The design system is rooted in the intersection of biological organicism and cultural preservation. It evokes a sense of serenity, growth, and timelessness. The brand personality is "The Modern Custodian"—respectful of heritage but forward-thinking in execution. 

The visual style is **Neumorphic-Organic**. It moves away from the clinical precision of flat design, embracing fluid, soft-molded surfaces that feel carved rather than built. Every interaction should feel like touching polished river stone or soft moss. By eliminating rigid boxes and sharp corners, the UI mimics the asymmetry found in nature, creating a seamless, immersive environment for exploring heritage and the natural world.

## Colors
The palette is a sophisticated blend of earth-tones and high-energy natural accents. The background uses a dual-tone approach, alternating between **Pale Mint** and **Light Cream** to define content zones without the need for harsh dividers.

**White** is reserved exclusively for foreground cards to provide a clean, elevated stage for high-resolution imagery. **Vibrant Lime/Olive** serves as the primary action color, ensuring high visibility and energy for key interactions. **Deep Forest Green** provides the necessary grounding for typography and the floating navigation, ensuring accessibility and a connection to the dense canopies of the natural world.

## Typography
The typography strategy balances modern approachability with high legibility. **Plus Jakarta Sans** is used for headings to provide a friendly, rounded geometric structure that complements the organic shapes of the UI. For body text and labels, **Be Vietnam Pro** offers a warm, contemporary feel that remains highly readable even during long-form heritage storytelling. 

Tight letter spacing is applied to large display headers to maintain a compact, "pebble-like" visual density, while labels receive increased tracking to ensure clarity against vibrant backgrounds.

## Layout & Spacing
This design system utilizes a **Fluid, Overlapping Layout** model. Instead of a traditional grid, elements are treated as independent layers that interact spatially. 

Key principles include:
- **Spatial Overlap:** Content cards frequently overlap hero images by -32px to create a sense of vertical depth and continuity.
- **Fluid Margins:** Use generous 24px side margins to allow the "floating" elements room to breathe.
- **Organic Grouping:** Rather than vertical stacking, utilize staggered horizontal placements for "blob" elements to mimic natural clusters.
- **Detached Navigation:** The primary navigation bar must never touch the screen edges, floating at least 16px above the bottom safe area.

## Elevation & Depth
Elevation is achieved through a combination of **Neumorphic extrusions** and **Ambient shadows**. Surfaces should not look like they are floating high above the background, but rather as if they are gently pushed out from the base material.

For white foreground cards, use dual-source soft shadows: one light shadow (top-left) and one slightly darker, tinted shadow (bottom-right) using the background color as a base. This creates a "soft-molded" appearance. Hero images utilize a **40% opacity black-to-transparent linear gradient** on the bottom third to ensure white typography remains legible without the need for high-contrast overlays.

## Shapes
The shape language is the core differentiator of the design system. It is defined by **Extreme Radiuses** and **Variable Curvature**. 

- **Hero Elements:** Use a massive 48px corner radius to soften the impact of large imagery.
- **Cards & Containers:** A minimum 32px radius is required, creating a "smooth stone" aesthetic.
- **Pill Shapes:** Interactive elements like buttons and tags always use a full pill radius.
- **Organic Blobs:** Background decorative elements should use CSS `border-radius` with 4-8 different values (e.g., `60% 40% 30% 70% / 60% 30% 70% 40%`) to create non-symmetrical, liquid shapes that evoke cells or leaves.

## Components
### Buttons
Primary buttons are high-energy **Vibrant Lime (#B7D23F)** pill-shapes with Deep Forest Green text. They should feature a subtle inner-glow to enhance the tactile, pressed-plastic feel.

### Floating Navigation
The navigation is a detached, pill-shaped bar in **Deep Forest Green (#0B2211)**. Icons should be minimal and rendered in Pale Mint with a soft active-state glow.

### Cards
White cards are the primary container for data. They must feature a 32px radius and use the dual-shadow neumorphic technique. Padding within cards is generous (24px) to avoid visual clutter.

### Image Heroes
Hero images should appear "tucked" into the layout, using the 48px radius. They should never be full-width unless they are the background of a section, in which case they should be masked by a large organic blob shape.

### Input Fields
Inputs are recessed into the Light Cream background using "inset" shadows, making them look like soft indentations in the UI surface.