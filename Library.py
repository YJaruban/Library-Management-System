import csv
import re
from datetime import datetime
import pandas as pd
import plotly.express as px

BOOK_FILE = "books.csv"
STUDENT_FILE = "students.csv"
TRANS_FILE = "transactions.csv"


# ---------------- FILE HANDLING ----------------
def load_csv(file):
    try:
        with open(file, newline='') as f:
            return list(csv.reader(f))
    except:
        return []

def save_csv(file, data):
    with open(file, "w", newline='') as f:
        writer = csv.writer(f)
        writer.writerows(data)


# ---------------- VALIDATIONS ----------------
def valid_book_id(book_id):
    return re.match(r"^[A-Za-z]{2}\d{2}$", book_id)

def valid_student_id(sid):
    return re.match(r"^\d{8}$", sid)

def valid_title(title):
    return title.isalpha() and len(title) <= 20

def valid_name(name):
    return name.isalpha() and len(name) <= 10

def valid_isbn13(isbn):
    if not re.match(r"^\d{13}$", isbn):
        return False
    total = 0
    for i, digit in enumerate(isbn[:12]):
        total += int(digit) * (1 if i % 2 == 0 else 3)
    check = (10 - (total % 10)) % 10
    return check == int(isbn[-1])

def valid_date(date):
    try:
        datetime.strptime(date, "%d/%m/%Y")
        return True
    except:
        return False


# ---------------- BOOK MANAGEMENT ----------------
def add_book():
    books = load_csv(BOOK_FILE)

    book_id = input("Book ID(LLDD): ")
    if not valid_book_id(book_id):
        print("Invalid Book ID")
        return

    isbn = input("ISBN-13: ")
    if not valid_isbn13(isbn):
        print("Invalid ISBN")
        return

    title = input("Title: ")
    if not all(x.isalpha() or x.isspace() for x in title) or len(title) > 20:
        print("Invalid Title")
        return

    author = input("Author: ")

    copies = int(input("Copies (max 2): "))
    if copies > 2 or copies < 1:
        print("Invalid copies")
        return

    price = float(input("Price: "))
    formatted_price = "{:.2f}" .format(price)

    books.append([book_id, title, isbn, copies, copies, price])
    save_csv(BOOK_FILE, books)
    print("Book added successfully!")


def view_books():
    books = load_csv(BOOK_FILE)
    for b in books:
        print(f"ID:{b[0]} | Title:{b[1]} | Available:{b[4]}")


def search_book():
    keyword = input("Search title: ").lower()
    books = load_csv(BOOK_FILE)
    for b in books:
        if keyword in b[1].lower():
            print(b)


def update_book():
    books = load_csv(BOOK_FILE)
    bid = input("Enter Book ID: ")

    for b in books:
        if b[0] == bid:
            b[1] = input("New Title: ")
            b[3] = input("New Copies: ")
            b[5] = input("New Price: ")
            save_csv(BOOK_FILE, books)
            print("Updated!")
            return

    print("Book not found")


# ---------------- STUDENT ----------------
def add_student():
    students = load_csv(STUDENT_FILE)

    sid = input("Student ID: ")
    if not valid_student_id(sid):
        print("Invalid ID")
        return

    name = input("Name: ")
    if not valid_name(name):
        print("Invalid Name")
        return

    students.append([sid, name])
    save_csv(STUDENT_FILE, students)
    print("Student added!")


# ---------------- ISSUE ----------------
def issue_book():
    books = load_csv(BOOK_FILE)
    students = load_csv(STUDENT_FILE)
    trans = load_csv(TRANS_FILE)

    bid = input("Book ID: ")
    sid = input("Student ID: ")
    date = input("Date (DD/MM/YYYY): ")

    if not valid_date(date):
        print("Invalid date")
        return

    # check student
    if not any(s[0] == sid for s in students):
        print("Invalid Student ID")
        return

    for b in books:
        if b[0] == bid:
            if int(b[4]) == 0:
                print("No copies available")
                return

            # prevent duplicate issue
            for t in trans:
                if t[1] == bid and t[2] == sid and t[3] == "1":
                    print("Already issued")
                    return

            b[4] = str(int(b[4]) - 1)
            trans.append([date, bid, sid, "1"])

            save_csv(BOOK_FILE, books)
            save_csv(TRANS_FILE, trans)
            print("Book issued!")
            return

    print("Invalid Book ID")


# ---------------- RETURN ----------------
def return_book():
    books = load_csv(BOOK_FILE)
    trans = load_csv(TRANS_FILE)

    bid = input("Book ID: ")
    sid = input("Student ID: ")
    date = input("Date(DD/MM/YYYY): ")

    def return_book():
        books = load_csv(BOOK_FILE)
        trans = load_csv(TRANS_FILE)

        bid = input("Book ID: ")
        sid = input("Student ID: ")
        date = input("Date (DD/MM/YYYY): ")

        # Check if this student actually has this book issued currently
        has_book = False
        for t in trans:
            if t[1] == bid and t[2] == sid:
                if t[3] == "1": has_book = True
                if t[3] == "2": has_book = False  # Reset if they already returned it

        if not has_book:
            print("Book already returned or never issued to this student")
            return

        # Update availability
        for b in books:
            if b[0] == bid:
                b[4] = str(int(b[4]) + 1)
                trans.append([date, bid, sid, "2"])
                save_csv(BOOK_FILE, books)
                save_csv(TRANS_FILE, trans)
                print("Returned!")
                return


    if not valid_date(date):
        print("Invalid date")
        return

    for b in books:
        if b[0] == bid:
            if int(b[4]) >= int(b[3]):
                print("Already returned")
                return

            b[4] = str(int(b[4]) + 1)
            trans.append([date, bid, sid, "2"])

            save_csv(BOOK_FILE, books)
            save_csv(TRANS_FILE, trans)
            print("Returned!")
            return

    print("Invalid Book ID")


# ---------------- GRAPH ----------------
def produce_graph():
    trans = load_csv(TRANS_FILE)

    data = [t for t in trans if t[3] == "1"]
    df = pd.DataFrame(data, columns=["date", "book", "student", "type"])

    if df.empty:
        print("No data")
        return

    df_count = df.groupby("date").size().reset_index(name="count")

    fig = px.line(df_count, x="date", y="count", title="Books Issued Trend")
    fig.show()


# ---------------- MENUS ----------------
def manage_books():
    while True:
        print("\n1.Add Book\n2.Update\n3.Search\n4.View\n5.Back")
        ch = input("Choice: ")

        if ch == "1": add_book()
        elif ch == "2": update_book()
        elif ch == "3": search_book()
        elif ch == "4": view_books()
        elif ch == "5": break


def main():
    while True:
        print("\n--- Library System ---")
        print("1.Manage Books")
        print("2.Add Student")
        print("3.Issue Book")
        print("4.Return Book")
        print("5.Produce Trend Graph")
        print("6.Exit")

        ch = input("\nHow can I help you today: ")

        if ch == "1": manage_books()
        elif ch == "2": add_student()
        elif ch == "3": issue_book()
        elif ch == "4": return_book()
        elif ch == "5": produce_graph()
        elif ch == "6": break


main()