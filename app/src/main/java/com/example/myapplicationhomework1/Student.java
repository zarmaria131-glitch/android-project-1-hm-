package com.example.myapplicationhomework1;

public class Student {
        private String firstName;
        private String lastName;
        private String email;
        private String grade;
        private String major;
        private boolean notifications;

        public Student(String firstName, String lastName, String email, String grade, String major, boolean notifications) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.email = email;
            this.grade = grade;
            this.major = major;
            this.notifications = notifications;
        }

        public String getFirstName() { return firstName; }
        public String getLastName() { return lastName; }
        public String getEmail() { return email; }
        public String getGrade() { return grade; }
        public String getMajor() { return major; }
        public boolean isNotifications() { return notifications; }

        @Override
        public String toString() {
            String notifText = notifications ? "כן" : "לא";
            return "שם: " + firstName + " " + lastName + "\n" +
                    "אימייל: " + email + "\n" +
                    "כיתה: " + grade + "\n" +
                    "מגמה: " + major + "\n" +
                    "התראות: " + notifText;
        }
}
