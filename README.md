# Driving Exam Preparation Platform

A web-based platform for practicing and simulating driving theory exams. The system focuses on timed tests, randomized question sets, and targeted practice on weak areas, while keeping user progress tracking intentionally minimal.

---

## 🎯 Project Goals

* Provide a clean and effective way to prepare for driving theory exams
* Simulate real exam conditions with time limits
* Allow focused practice through random tests and wrong-answer review
* Keep the system simple, fast, and easy to maintain

---

## 🚀 Core Features (MVP)

### 1. User Authentication

* User registration and login via **email and password**
* **Google OAuth** for quick sign-in
* Basic account management:

  * Password reset
  * Logout

---

### 2. Admin Panel (Content Management)

* Dedicated admin interface for managing exam content
* Create, edit, and delete:

  * Questions
  * Answer options
  * Correct answers
* Enable or disable questions without permanent deletion
* Assign questions to categories or exam templates

---

### 3. Exam Templates (Read-Only for Users)

* Users can access predefined exam templates
* Templates are **read-only**
* Each template:

  * Contains a fixed set of questions
  * Has a **25-minute time limit**
  * Is automatically submitted when the time expires

---

### 4. Random Test Generator

* Generate randomized exams with:

  * **20, 50, or 100 questions**
* Questions are randomly selected from the global question pool
* No duplicate questions within a single test
* Each attempt generates a new question set

---

### 5. All Questions & Search

* Dedicated **“All Questions”** section
* Full-text search across question content
* Optional filtering by category or topic
* Users can view questions and reveal correct answers on demand

---

### 6. Result Tracking (Minimal)

* No per-question or per-template progress tracking
* Store only:

  * Last test result
  * Best test result
  * Number of attempts
* Results are used only for user feedback

---

### 7. Flag / Mark for Review

* Users can **mark questions for review** during a test
* Marked questions are easily accessible before final submission
* Review marks are cleared after the test is completed

---

### 8. Wrong Answers History

* Maintain a history of questions answered incorrectly by the user
* Users can review wrong answers to focus on weak areas
* History updates after each completed test

---

## 📌 Future Improvements (Optional)

* Category-based random tests
* CSV/JSON import for bulk question management

---

## 🛠️ Status

This project is currently in **active development** and focused on delivering a clean MVP based on the features above.
