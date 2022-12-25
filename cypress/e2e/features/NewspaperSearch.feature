Feature: Newspaper search
  Scenario: Search for 'The Age' newspaper in google archive by year
    Given : I open Google archive
    And : click on 'The Age' newspaper
    When : I search by year when newspaper was printed
    Then : I see scans of the newspaper


  Scenario: Search for 'The Age' newspaper in google archive by year
      Given : I open Google archive
      And : click on 'The Age' newspaper
      When : I search by year & month when newspaper was printed
      Then : I see scans of the newspaper

  #negative scenario
  Scenario: Search for 'The Age' newspaper in google archive by year
    Given : I open Google archive
    And : click on 'The Age' newspaper
    When : I search by year in the future
    Then : I don't see any scans
