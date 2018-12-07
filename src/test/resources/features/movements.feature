Feature: Movement

  Scenario: Retrieve a list of recent movements

    Acceptance Criteria:
    A batch system user can retrieve a list of offenders with recent movements

    Given a user has a token name of "GLOBAL_SEARCH"
    When a request is made to retrieve recent movements
    Then a correct list of records are returned

  Scenario Outline: Retrieve a list of recent movements for offenders
    Given a user has a token name of "GLOBAL_SEARCH"
    When a make a request for recent movements for "A6676RS" and "Z0021ZZ"
    Then the records should contain a entry for "<movementType>" "<fromDescription>" "<toDescription>" "<reasonDescription>" "<movementTime>"
    Examples:
      | movementType| fromDescription    | toDescription | reasonDescription                | movementTime |
      | TRN         |  Birmingham        |  Moorland     | Normal Transfer                  | 12:00:00     |
      | REL         |  Leeds             |  Outside      | Abscond End of Custody Licence   | 00:00:00     |

  Scenario: Get the establishment roll count for a prison

    Acceptance Criteria:
    A logged in user can retrieve a prison's establishment roll count

    Given a user has authenticated with the API
    When a request is made to retrieve the establishment roll count for an agency
    Then a valid list of roll count records are returned

  Scenario: Get the establishment unassigned roll count for a prison

    Acceptance Criteria:
    A logged in user can retrieve a prison's establishment unassigned roll count

    Given a user has authenticated with the API
    When a request is made to retrieve the establishment unassigned roll count for an agency
    Then a valid list of unassigned roll count records are returned

  Scenario: Get a days movement count for a prison

    Acceptance Criteria:
    A logged in user can get the count of prisoners in and out on a specific day

    Given a user has authenticated with the API
    When a request is made to retrieve the movement counts for an agency on "2017-08-16"
    Then a total count of out today as "2" offender numbers that are out today matching "Z0021ZZ,Z0019ZZ" and a count of in today as "0""

  Scenario Outline: Retrieve a list of en-route offenders
    Given a user has authenticated with the API
    When a request is made for en-route offenders for agency "LEI" on movement date "2017-10-12"
    Then the records should contain a entry for "<offenderNo>" "<lastName>" "<fromAgencyDescription>" "<toAgencyDescription>" "<reasonDescription>" "<movementTime>"
    Examples:
      | offenderNo  | fromAgencyDescription | toAgencyDescription  | movementTime   | reasonDescription    | lastName  |
      | A1183AD     |  Birmingham           | Leeds                |  15:00:00      | Normal Transfer      | DENTON    |
      | A1183SH     |  Birmingham           | Leeds                |  13:00:00      | Normal Transfer      | HEMP      |