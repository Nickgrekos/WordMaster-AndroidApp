![WordMaster Demo]⬇️

https://github.com/user-attachments/assets/d151f865-b887-488c-a289-851fc15a403b

<img width="1254" height="1254" alt="203d0454-3f63-4c14-aec2-37bdc82f421b" src="https://github.com/user-attachments/assets/da9235e5-b2d8-44a1-9d4f-670308489f58" />


# 🎯 WordMaster

> A Greek party word game for Android — describe it, mime it, say it in one word.

WordMaster is a multiplayer party game for **2 teams** and **4–25 players**. Each player secretly adds words to a shared pool, then teams compete across three rounds to guess them all: first with descriptions, then with mime, then with just a single word. It's a memory game wrapped in a guessing game.

---

# ❔ Full game instructions in greek 
-Κανόνες Παιχνιδιού
Το παιχνίδι παίζεται σε δύο ομάδες και αποτελείται από τρεις γύρους. Στην αρχή, κάθε παίκτης προσθέτει τις λέξεις του μέσα στην εφαρμογή. Όλες οι λέξεις συγκεντρώνονται αυτόματα σε ένα κοινό σύνολο, το οποίο χρησιμοποιείται σε όλη τη διάρκεια του παιχνιδιού.

Οι ομάδες παίζουν εναλλάξ. Κάθε φορά που μια ομάδα ξεκινά τη σειρά της, ένας παίκτης αναλαμβάνει τον ρόλο του αρχηγού (captain). Ο αρχηγός είναι ο μόνος που βλέπει τη λέξη στην οθόνη και προσπαθεί να βοηθήσει την ομάδα του να τη μαντέψει. Σε κάθε νέα σειρά, ο ρόλος του αρχηγού αλλάζει, ώστε όλοι οι παίκτες να συμμετέχουν.

Κάθε σειρά έχει χρονικό όριο 60 δευτερολέπτων. Μέσα σε αυτό το διάστημα, η ομάδα προσπαθεί να βρει όσο το δυνατόν περισσότερες λέξεις. Για κάθε σωστή απάντηση καταγράφεται ένας πόντος, ενώ οι λέξεις που παραλείπονται επιστρέφουν στο σύνολο.

Κάθε γύρος συνεχίζεται με εναλλαγές μεταξύ των ομάδων μέχρι να εξαντληθούν όλες οι λέξεις. Όταν ολοκληρωθεί ένας γύρος, το ίδιο σύνολο λέξεων επαναχρησιμοποιείται από την αρχή για τον επόμενο.

Γύρος 1: Περιγραφή
Ο αρχηγός μπορεί να περιγράψει τη λέξη ελεύθερα, χωρίς να την πει αυτούσια. Η ομάδα μαντεύει βασιζόμενη στις περιγραφές.

Γύρος 2: Παντομίμα
Ο αρχηγός δεν επιτρέπεται να μιλήσει και χρησιμοποιεί μόνο κινήσεις για να βοηθήσει την ομάδα του να μαντέψει.

Γύρος 3: Μία Λέξη
Ο αρχηγός μπορεί να πει μόνο μία λέξη για κάθε ζητούμενη λέξη. Κάθε παίκτης της ομάδας έχει μία μόνο προσπάθεια να μαντέψει. Αν η λέξη δεν βρεθεί, επιστρέφει στο σύνολο.

Μετά την ολοκλήρωση και των τριών γύρων, οι πόντοι των ομάδων αθροίζονται και η ομάδα με το υψηλότερο σκορ αναδεικνύεται νικήτρια.

---

## 📸 Screens

| Main Menu | Game Setup | Word Entry | Gameplay | Winner |
|-----------|------------|------------|----------|--------|
| Play & How To Play | Team names + player/word count | Each player submits their words privately | 60-second countdown per turn | Scores, winner, confetti 🎉 |

---

## 🎮 How It Works

1. **Setup** — Choose how many players (4–25) and words per player (3–10), then name your two teams.
2. **Word Entry** — Each player privately types their words into the shared pool. No peeking.
3. **Round 1 – Περιγραφή (Description)** — Describe the word without saying it. Your team guesses as many as possible in 60 seconds.
4. **Round 2 – Παντομίμα (Mime)** — Same words, no talking. Act it out.
5. **Round 3 – Μία Λέξη (One Word)** — Same words again. You get exactly one word as a clue.
6. **Winner** — The team with the most points across all three rounds wins.

> The twist: because the same words repeat across all three rounds, players slowly memorize the pool — making Round 3 feel like a shared inside joke.

---

## 🏗️ Architecture

```
com.example.wordmaster
├── BaseActivity.java          # Edge-to-edge insets base class
├── MainActivity.java          # Entry point: Play / How To Play
├── GameActivity.java          # Game setup: player count, word count, team names
├── WordsCreationActivity.java # Word submission, one player at a time
├── TurnActivity.java          # Between turns: scores, round info, next team
├── GameplayActivity.java      # Live gameplay: 60s timer, skip / correct
├── WinnerActivity.java        # End screen with final scores and confetti
├── ConfettiView.java          # Custom animated confetti view
└── DatabaseHelper.java        # SQLite: words pool + team scores
```

### Data Flow

```
GameActivity
    └─► WordsCreationActivity  (inserts words into SQLite)
            └─► TurnActivity   (reads scores + round state)
                    └─► GameplayActivity  (marks words used, updates scores)
                            └─► TurnActivity  (loops until all rounds done)
                                    └─► WinnerActivity
```

---

## 🗄️ Database Schema

**`words`**
| Column | Type | Description |
|--------|------|-------------|
| `id` | INTEGER PK | Auto-increment |
| `player_number` | INTEGER | Which player submitted this word |
| `word` | TEXT | The word itself |
| `is_used` | INTEGER | 0 = available, 1 = guessed this round |

**`teams`**
| Column | Type | Description |
|--------|------|-------------|
| `id` | INTEGER PK | Auto-increment |
| `team_name` | TEXT | Team display name |
| `score` | INTEGER | Cumulative score across all rounds |

> At the end of each round, `is_used` is reset to `0` for all words so the same pool repeats. Scores are **cumulative** — they carry over across rounds.

---

## 🛠️ Tech Stack

- **Language:** Java
- **Min SDK:** Android (Edge-to-Edge support via `androidx.core`)
- **Database:** SQLite via `SQLiteOpenHelper`
- **UI:** XML layouts + Material Snackbar
- **Dependencies:** AndroidX AppCompat, Material Components, Core-KTX

---

## 📋 Game Rules at a Glance

| | Round 1 | Round 2 | Round 3 |
|-|---------|---------|---------|
| **Name** | Περιγραφή | Παντομίμα | Μία Λέξη |
| **Allowed** | Any words/gestures | No talking | One word only |
| **Time** | 60 seconds | 60 seconds | 60 seconds |
| **Skip** | ✅ Yes | ✅ Yes | ✅ Yes |

- Skipped words stay in the pool and can appear again later in the same turn.
- If the word pool empties mid-turn, the turn ends early.
- The game ends after all three rounds are complete.

---

## 🤝 Contributing

Pull requests are welcome. For major changes please open an issue first.

- Keep UI strings in `res/values/strings.xml` for localization support.
- Use `ViewModel` + `onSaveInstanceState` to preserve state across rotations rather than locking screen orientation.
- When changing the DB schema, add a proper migration in `onUpgrade()` to avoid data loss on update.
- When adding new activities, extend `BaseActivity` to get Edge-to-Edge insets for free.

---

## 🗺️ Roadmap

- [ ] Color selection per team
- [ ] Additional game settings customization (ability to skip toggle, timmer modifier)
- [ ] English / Greek language toggle
- [ ] Improve main screen UI
- [ ] Unit and UI tests for the full game flow

---

## 📄 License

No license yet.
