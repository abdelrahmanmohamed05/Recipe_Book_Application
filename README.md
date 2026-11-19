# 🍳 Recipe Book Application

A comprehensive, full-featured Android Recipe Management application designed and developed collaboratively. The project employs a robust **Hybrid UI approach** combining modern **Jetpack Compose** and **XML Layout Views (RecyclerView, MaterialCardView, ConstraintLayout)**, powered by **Room Database**, **DataStore Preferences**, and **Kotlin Coroutines / Flow** under a clean **MVVM architecture**.

---

## 🛠️ Tech Stack & Architecture
- **Architecture:** MVVM (Model-View-ViewModel) with Repository pattern.
- **UI Frameworks (Hybrid Approach):**
  - **Jetpack Compose & Material 3:** For dynamic theming, navigation graph, settings, and modern components.
  - **XML Views & Material Components:** Utilizing `RecyclerView`, `MaterialCardView`, `ConstraintLayout`, and View Binding for high-performance lists and recipe item cards.
- **Local Persistence:** Room Database with KSP (Kotlin Symbol Processing) and relational entities (`Recipe`, `Ingredient`, `Step`, `RecipeWithDetails`).
- **Preferences:** Jetpack DataStore Preferences for persistent Dark Mode and user settings.
- **Navigation:** Jetpack Navigation Compose (`home`, `favorites`, `detail/{id}`, `addEdit/{id}`, `settings`).
- **Concurrency:** Kotlin Coroutines & Flows with lifecycle-aware state collection (`collectAsStateWithLifecycle`).

---

## 📱 Core Features
1. **Recipe Feed & Home Screen:** 
   - Browse recipes fetched reactively from Room Database.
   - Real-time search filtering by title and description.
   - Quick toggle for favorite bookmarking.
   - Automatic database seeding on first launch (pre-populated with starter recipes: Koshari, Pancakes, Greek Salad).
2. **Recipe Detail View:**
   - Detailed breakdown of ingredients, cooking steps, preparation time, category, and servings.
   - Built-in recipe sharing utility.
   - Direct actions to edit or delete recipes with confirmation dialogs.
3. **Add / Edit Recipe Form:**
   - Dynamic addition and removal of ingredient rows and cooking steps.
   - Real-time form validation.
4. **Favorites Screen:**
   - Dedicated view for all user-bookmarked favorite recipes.
5. **Settings Screen:**
   - Persistent Dark Mode toggle backed by Jetpack DataStore.

---

## 🗄️ Database Schema & Entities
- **Recipe:** Stores core recipe metadata (`id`, `title`, `description`, `category`, `prepTimeMinutes`, `servings`, `isFavorite`).
- **Ingredient:** Relational table linked by `recipeId` (`name`, `quantity`).
- **Step:** Relational table linked by `recipeId` (`stepNumber`, `instruction`).
- **RecipeWithDetails:** Room `@Relation` data class combining `Recipe` with its respective `Ingredient` list and `Step` list.

---

## 📂 Project Package Structure
```text
com.example.myapplication/
├── data/                  # Room Database, DAOs, Entities (Recipe, Ingredient, Step), Repositories
├── ui/
│   ├── addedit/           # Add/Edit XML layouts, RecipeForm, and form validation logic
│   ├── components/        # Reusable UI components (RecipeBottomBar)
│   ├── detail/            # Detail screen views and sharing utilities
│   ├── navigation/        # Navigation graph (RecipeNavGraph) and Screen routes
│   ├── screens/           # Compose screen wrappers (HomeScreen, FavoritesScreen, SettingsScreen, etc.)
│   └── theme/             # Material 3 Theme (Color, Type, Theme)
├── util/                  # Helper utilities (ShareUtils)
└── viewmodel/             # ViewModels (RecipeListViewModel, RecipeDetailViewModel, AddEditRecipeViewModel, SettingsViewModel) & ViewModelFactory
```

---

## 👥 Team Collaboration & Development
- Developed collaboratively using daily task breakdowns and modular contributions.
- Hybrid integration allowing seamless interoperation between XML-based views and Jetpack Compose destinations.
- Fully tested and built successfully (`app:assembleDebug`).
