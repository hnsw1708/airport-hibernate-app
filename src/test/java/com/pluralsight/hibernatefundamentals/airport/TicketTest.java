package com.pluralsight.hibernatefundamentals.airport;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Ticket entity tests")
class TicketTest {

    // ---------------------------------------------------------------------------
    // Construction
    // ---------------------------------------------------------------------------

    @Nested
    @DisplayName("Constructor tests")
    class ConstructorTests {

        @Test
        @DisplayName("No-arg constructor creates a ticket with default values")
        void noArgConstructor_createsTicketWithDefaults() {
            Ticket ticket = new Ticket();

            assertEquals(0, ticket.getId(),     "Default id should be 0");
            assertNull(ticket.getNumber(),      "Default number should be null");
            assertNull(ticket.getPassenger(),   "Default passenger should be null");
        }

        @Test
        @DisplayName("Parameterised constructor sets id and number correctly")
        void parameterisedConstructor_setsIdAndNumber() {
            Ticket ticket = new Ticket(42, "TK-001");

            assertAll(
                () -> assertEquals(42,       ticket.getId()),
                () -> assertEquals("TK-001", ticket.getNumber()),
                () -> assertNull(ticket.getPassenger())
            );
        }

        @Test
        @DisplayName("Parameterised constructor with id = 0 is valid")
        void parameterisedConstructor_withZeroId_isValid() {
            Ticket ticket = new Ticket(0, "TK-ZERO");

            assertEquals(0,         ticket.getId());
            assertEquals("TK-ZERO", ticket.getNumber());
        }

        @Test
        @DisplayName("Parameterised constructor with negative id is stored as-is")
        void parameterisedConstructor_withNegativeId_storedAsIs() {
            Ticket ticket = new Ticket(-1, "TK-NEG");

            assertEquals(-1, ticket.getId());
        }

        @Test
        @DisplayName("Parameterised constructor with null number stores null")
        void parameterisedConstructor_withNullNumber_storesNull() {
            Ticket ticket = new Ticket(1, null);

            assertNull(ticket.getNumber());
        }
    }

    // ---------------------------------------------------------------------------
    // setId / getId
    // ---------------------------------------------------------------------------

    @Nested
    @DisplayName("id property tests")
    class IdPropertyTests {

        @Test
        @DisplayName("setId updates the id returned by getId")
        void setId_updatesId() {
            Ticket ticket = new Ticket();
            ticket.setId(99);

            assertEquals(99, ticket.getId());
        }

        @ParameterizedTest(name = "setId({0}) is accepted")
        @ValueSource(ints = {Integer.MIN_VALUE, -1, 0, 1, Integer.MAX_VALUE})
        @DisplayName("setId accepts boundary int values")
        void setId_acceptsBoundaryValues(int id) {
            Ticket ticket = new Ticket();
            ticket.setId(id);

            assertEquals(id, ticket.getId());
        }

        @Test
        @DisplayName("setId can overwrite an existing id")
        void setId_canOverwriteExistingId() {
            Ticket ticket = new Ticket(10, "TK-010");
            ticket.setId(20);

            assertEquals(20, ticket.getId());
        }
    }

    // ---------------------------------------------------------------------------
    // setNumber / getNumber
    // ---------------------------------------------------------------------------

    @Nested
    @DisplayName("number property tests")
    class NumberPropertyTests {

        @Test
        @DisplayName("setNumber updates the value returned by getNumber")
        void setNumber_updatesNumber() {
            Ticket ticket = new Ticket();
            ticket.setNumber("TK-555");

            assertEquals("TK-555", ticket.getNumber());
        }

        @ParameterizedTest
        @NullAndEmptySource
        @DisplayName("setNumber accepts null and empty string")
        void setNumber_acceptsNullAndEmpty(String value) {
            Ticket ticket = new Ticket();
            ticket.setNumber(value);

            assertEquals(value, ticket.getNumber());
        }

        @Test
        @DisplayName("setNumber can overwrite an existing number")
        void setNumber_canOverwriteExistingNumber() {
            Ticket ticket = new Ticket(1, "OLD-NUMBER");
            ticket.setNumber("NEW-NUMBER");

            assertEquals("NEW-NUMBER", ticket.getNumber());
        }

        @Test
        @DisplayName("setNumber stores a long string correctly (text block used for readability)")
        void setNumber_longString() {
            // Java 21 text block used for a multiline-style long value
            String longNumber = """
                    TICKET-VERY-LONG-NUMBER-12345678901234567890""".strip();

            Ticket ticket = new Ticket();
            ticket.setNumber(longNumber);

            assertEquals(longNumber, ticket.getNumber());
        }
    }

    // ---------------------------------------------------------------------------
    // setPassenger / getPassenger
    // ---------------------------------------------------------------------------

    @Nested
    @DisplayName("passenger property tests")
    class PassengerPropertyTests {

        @Test
        @DisplayName("setPassenger stores the passenger returned by getPassenger")
        void setPassenger_storesPassenger() {
            Ticket ticket = new Ticket();
            Passenger passenger = new Passenger();

            ticket.setPassenger(passenger);

            assertSame(passenger, ticket.getPassenger());
        }

        @Test
        @DisplayName("setPassenger with null clears the passenger")
        void setPassenger_withNull_clearsPassenger() {
            Ticket ticket = new Ticket();
            Passenger passenger = new Passenger();
            ticket.setPassenger(passenger);

            ticket.setPassenger(null);

            assertNull(ticket.getPassenger());
        }

        @Test
        @DisplayName("setPassenger can replace an existing passenger with a different one")
        void setPassenger_replacesExistingPassenger() {
            Ticket ticket  = new Ticket();
            Passenger first  = new Passenger();
            Passenger second = new Passenger();

            ticket.setPassenger(first);
            ticket.setPassenger(second);

            assertSame(second, ticket.getPassenger());
        }

        @Test
        @DisplayName("Passenger relationship is independent of ticket number")
        void passengerRelationship_isIndependentOfTicketNumber() {
            Passenger passenger = new Passenger();
            Ticket ticket = new Ticket(7, "TK-007");
            ticket.setPassenger(passenger);

            assertAll(
                () -> assertEquals(7,        ticket.getId()),
                () -> assertEquals("TK-007", ticket.getNumber()),
                () -> assertSame(passenger,  ticket.getPassenger())
            );
        }
    }

    // ---------------------------------------------------------------------------
    // Java 21 pattern-matching / record-style integration smoke test
    // ---------------------------------------------------------------------------

    @Nested
    @DisplayName("Java 21 feature integration")
    class Java21FeatureTests {

        /**
         * A local record used to capture expected ticket state in a readable way.
         */
        record TicketSnapshot(int id, String number) {}

        @Test
        @DisplayName("Ticket state matches snapshot after setters are called")
        void ticketState_matchesSnapshot() {
            var expected = new TicketSnapshot(5, "TK-005");

            Ticket ticket = new Ticket();
            ticket.setId(expected.id());
            ticket.setNumber(expected.number());

            var actual = new TicketSnapshot(ticket.getId(), ticket.getNumber());

            assertEquals(expected, actual);
        }

        @Test
        @DisplayName("Pattern matching instanceof works on Ticket")
        void patternMatching_instanceofTicket() {
            Object obj = new Ticket(3, "TK-003");

            // Java 21 pattern-matching instanceof
            if (obj instanceof Ticket t) {
                assertAll(
                    () -> assertEquals(3,        t.getId()),
                    () -> assertEquals("TK-003", t.getNumber())
                );
            } else {
                fail("Object should be a Ticket");
            }
        }

        @Test
        @DisplayName("Switch expression with pattern matching resolves Ticket type")
        void switchExpression_patternMatchingOnTicket() {
            Object obj = new Ticket(8, "TK-008");

            String result = switch (obj) {
                case Ticket t -> "Ticket id=%d number=%s".formatted(t.getId(), t.getNumber());
                default       -> "unknown";
            };

            assertEquals("Ticket id=8 number=TK-008", result);
        }
    }
}
