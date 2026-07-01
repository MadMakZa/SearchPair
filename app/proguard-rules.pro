# Crash reports: keep line numbers, hide original file names
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile

# Kotlin / coroutines
-dontwarn kotlin.**
-dontwarn kotlinx.coroutines.**

# ViewModels and their Factory inner classes
-keep class * extends androidx.lifecycle.ViewModel {
    <init>(...);
}
-keep class * implements androidx.lifecycle.ViewModelProvider$Factory {
    public androidx.lifecycle.ViewModel create(java.lang.Class);
}

# Navigation routes
-keep class makza.afonsky.searchpair.navigation.** { *; }

# Game state models used across layers
-keep class makza.afonsky.searchpair.game.GameCard { *; }
-keep class makza.afonsky.searchpair.game.GameUiState { *; }
-keep class makza.afonsky.searchpair.game.GamePhase { *; }
-keep class makza.afonsky.searchpair.game.GameEvent { *; }
-keep class makza.afonsky.searchpair.game.GameEvent$* { *; }
-keep class makza.afonsky.searchpair.data.** { *; }
-keep class makza.afonsky.searchpair.ui.menu.MenuUiState { *; }

# Enums
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# Compose / AndroidX — consumer rules from dependencies cover most cases
-dontwarn androidx.compose.**
