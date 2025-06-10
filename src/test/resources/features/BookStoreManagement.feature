Feature: Book store management

Scenario: User manages books
  Given user is logged in
  When user creates a new book
  Then bookId is returned
  When user retrieves the book by id
  Then book details are returned
  When user deletes the book by id
  Then the book is deleted

Scenario: Retrieve non-existent book
  Given user is logged in
  When user retrieves the book with id "404-id"
  Then the API should return status 404 and message "Book not found"
