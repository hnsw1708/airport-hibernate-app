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
    @DisplayName("No-arg constructor")
    class NoArgConstructor {

        @Test
        @DisplayName("creates a Ticket with default int id = 0 and null fields")
        void defaultConstructorInitialisesDefaults() {
            var ticket = new Ticket();

            assertEquals(0, ticket.getId());
            assertNull(ticket.getNumber());
            assertNull(ticket.getPassenger());
        }
    }

    @Nested
    @DisplayName("Parameterised constructor (id, number)")
    class ParameterisedConstructor {

        @Test
        @DisplayName("stores supplied id and number")
        void constructorStoresValues() {
            var ticket = new Ticket(42, "TKT-001");

            assertEquals(42, ticket.getId());
            assertEquals("TKT-001", ticket.getNumber());
        }

        @Test
        @DisplayName("passenger is null when created via parameterised constructor")
        void passengerIsNullAfterParameterisedConstruction() {
            var ticket = new Ticket(1, "TKT-100");

            assertNull(ticket.getPassenger());
        }

        @Test
        @DisplayName("id zero is a valid edge-case value")
        void idZeroIsAccepted() {
            var ticket = new Ticket(0, "TKT-ZERO");

            assertEquals(0, ticket.getId());
        }

        @Test
        @DisplayName("negative id is stored as supplied")
        void negativeIdIsStoredAsIs() {
            var ticket = new Ticket(-1, "TKT-NEG");

            assertEquals(-1, ticket.getId());
        }

        @Test
        @DisplayName("null number is stored without NPE")
        void nullNumberIsAccepted() {
            var ticket = new Ticket(5, null);

            assertNull(ticket.getNumber());
        }

        @Test
        @DisplayName("empty string number is stored correctly")
        void emptyNumberIsAccepted() {
            var ticket = new Ticket(6, "");

            assertEquals("", ticket.getNumber());
        }
    }

    // ---------------------------------------------------------------------------
    // setId / getId
    // ---------------------------------------------------------------------------

    @Nested
    @DisplayName("id getter / setter")
    class IdGetterSetter {

        @Test
        @DisplayName("setId updates the value returned by getId")
        void setIdUpdatesId() {
            var ticket = new Ticket();
            ticket.setId(99);

            assertEquals(99, ticket.getId());
        }

        @Test
        @DisplayName("setId with zero is accepted")
        void setIdZero() {
            var ticket = new Ticket(10, "X");
            ticket.setId(0);

            assertEquals(0, ticket.getId());
        }

        @Test
        @DisplayName("setId with Integer.MAX_VALUE is accepted")
        void setIdMaxInt() {
            var ticket = new Ticket();
            ticket.setId(Integer.MAX_VALUE);

            assertEquals(Integer.MAX_VALUE, ticket.getId());
        }

        @Test
        @DisplayName("setId with Integer.MIN_VALUE is accepted")
        void setIdMinInt() {
            var ticket = new Ticket();
            ticket.setId(Integer.MIN_VALUE);

            assertEquals(Integer.MIN_VALUE, ticket.getId());
        }
    }

    // ---------------------------------------------------------------------------
    // setNumber / getNumber
    // ---------------------------------------------------------------------------

    @Nested
    @DisplayName("number getter / setter")
    class NumberGetterSetter {

        @Test
        @DisplayName("setNumber updates the value returned by getNumber")
        void setNumberUpdatesNumber() {
            var ticket = new Ticket();
            ticket.setNumber("ABC-123");

            assertEquals("ABC-123", ticket.getNumber());
        }

        @ParameterizedTest(name = "setNumber with blank/null value [{0}] stores as-is")
        @NullAndEmptySource
        @DisplayName("null and empty number values are stored without NPE")
        void setNumberNullAndEmpty(String value) {
            var ticket = new Ticket();
            ticket.setNumber(value);

            assertEquals(value, ticket.getNumber());
        }

        @ParameterizedTest(name = "number [{0}] round-trips correctly")
        @ValueSource(strings = {"TKT-001", "  whitespace  ", "\u7279\u6B8A\u6587\u5B57", "1234567890"})
        @DisplayName("various number formats round-trip through setter/getter")
        void setNumberRoundTrip(String number) {
            var ticket = new Ticket();
            ticket.setNumber(number);

            assertEquals(number, ticket.getNumber());
        }

        @Test
        @DisplayName("number can be overwritten multiple times")
        void numberCanBeOverwritten() {
            var ticket = new Ticket(1, "FIRST");
            ticket.setNumber("SECOND");
            ticket.setNumber("THIRD");

            assertEquals("THIRD", ticket.getNumber());
        }

        @Test
        @DisplayName("long ticket number (text block) is stored correctly")
        void longTicketNumberStoredCorrectly() {
            var longNumber = """
                    VERY-LONG-TICKET-NUMBER-0000000001""".strip();

            var ticket = new Ticket();
            ticket.setNumber(longNumber);

            assertEquals("VERY-LONG-TICKET-NUMBER-0000000001", ticket.getNumber());
        }
    }

    // ---------------------------------------------------------------------------
    // setPassenger / getPassenger
    // ---------------------------------------------------------------------------

    @Nested
    @DisplayName("passenger getter / setter")
    class PassengerGetterSetter {

        @Test
        @DisplayName("setPassenger stores the passenger and getPassenger returns it")
        void setPassengerStoresPassenger() {
            var ticket = new Ticket();
            var passenger = new Passenger();

            ticket.setPassenger(passenger);

            assertSame(passenger, ticket.getPassenger());
        }

        @Test
        @DisplayName("setPassenger with null clears the passenger association")
        void setPassengerNullClearsAssociation() {
            var passenger = new Passenger();
            var ticket = new Ticket();
            ticket.setPassenger(passenger);

            ticket.setPassenger(null);

            assertNull(ticket.getPassenger());
        }

        @Test
        @DisplayName("passenger can be replaced with a different instance")
        void passengerCanBeReplaced() {
            var firstPassenger = new Passenger();
            var secondPassenger = new Passenger();
            var ticket = new Ticket();

            ticket.setPassenger(firstPassenger);
            ticket.setPassenger(secondPassenger);

            assertSame(secondPassenger, ticket.getPassenger());
        }
    }

    // ---------------------------------------------------------------------------
    // Combined / integration-style scenarios
    // ---------------------------------------------------------------------------

    @Nested
    @DisplayName("Combined field mutation scenarios")
    class CombinedScenarios {

        @Test
        @DisplayName("all fields can be set independently via setters")
        void allFieldsCanBeSetIndependently() {
            var ticket = new Ticket();
            var passenger = new Passenger();

            ticket.setId(10);
            ticket.setNumber("COMBO-001");
            ticket.setPassenger(passenger);

            assertEquals(10, ticket.getId());
            assertEquals("COMBO-001", ticket.getNumber());
            assertSame(passenger, ticket.getPassenger());
        }

        @Test
        @DisplayName("constructor values can be overridden by setters")
        void constructorValuesCanBeOverridden() {
            var ticket = new Ticket(1, "ORIGINAL");
            ticket.setId(2);
            ticket.setNumber("OVERRIDDEN");

            assertEquals(2, ticket.getId());
            assertEquals("OVERRIDDEN", ticket.getNumber());
        }
    }
}