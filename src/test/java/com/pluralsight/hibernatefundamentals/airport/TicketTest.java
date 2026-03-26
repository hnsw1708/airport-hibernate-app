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
        @DisplayName("creates instance with default values")
        void defaultValues() {
            Ticket ticket = new Ticket();

            assertAll(
                    () -> assertEquals(0, ticket.getId(),       "default id should be 0"),
                    () -> assertNull(ticket.getNumber(),        "default number should be null"),
                    () -> assertNull(ticket.getPassenger(),     "default passenger should be null")
            );
        }
    }

    @Nested
    @DisplayName("Parameterised constructor")
    class ParameterisedConstructor {

        @Test
        @DisplayName("stores id and number correctly")
        void storesIdAndNumber() {
            var ticket = new Ticket(42, "TK-001");

            assertAll(
                    () -> assertEquals(42,       ticket.getId()),
                    () -> assertEquals("TK-001", ticket.getNumber()),
                    () -> assertNull(ticket.getPassenger(), "passenger not set in 2-arg constructor")
            );
        }

        @Test
        @DisplayName("stores zero id")
        void zeroId() {
            var ticket = new Ticket(0, "ZERO");
            assertEquals(0, ticket.getId());
        }

        @Test
        @DisplayName("stores negative id")
        void negativeId() {
            var ticket = new Ticket(-1, "NEG");
            assertEquals(-1, ticket.getId());
        }

        @Test
        @DisplayName("accepts null number without throwing")
        void nullNumber() {
            assertDoesNotThrow(() -> new Ticket(1, null));
            var ticket = new Ticket(1, null);
            assertNull(ticket.getNumber());
        }

        @Test
        @DisplayName("accepts empty number without throwing")
        void emptyNumber() {
            var ticket = new Ticket(1, "");
            assertEquals("", ticket.getNumber());
        }
    }

    // ---------------------------------------------------------------------------
    // Getters / Setters
    // ---------------------------------------------------------------------------

    @Nested
    @DisplayName("setId / getId")
    class IdAccessors {

        @Test
        @DisplayName("round-trips a positive id")
        void positiveId() {
            var ticket = new Ticket();
            ticket.setId(99);
            assertEquals(99, ticket.getId());
        }

        @Test
        @DisplayName("round-trips zero")
        void zeroId() {
            var ticket = new Ticket();
            ticket.setId(0);
            assertEquals(0, ticket.getId());
        }

        @Test
        @DisplayName("round-trips a negative id")
        void negativeId() {
            var ticket = new Ticket();
            ticket.setId(-5);
            assertEquals(-5, ticket.getId());
        }

        @Test
        @DisplayName("overwrites an existing id")
        void overwriteId() {
            var ticket = new Ticket(1, "X");
            ticket.setId(100);
            assertEquals(100, ticket.getId());
        }
    }

    @Nested
    @DisplayName("setNumber / getNumber")
    class NumberAccessors {

        @Test
        @DisplayName("round-trips a typical ticket number")
        void typicalNumber() {
            var ticket = new Ticket();
            ticket.setNumber("TK-2024");
            assertEquals("TK-2024", ticket.getNumber());
        }

        @ParameterizedTest(name = "null or empty number: [{0}]")
        @NullAndEmptySource
        @DisplayName("accepts null and empty number")
        void nullAndEmptyNumber(String value) {
            var ticket = new Ticket();
            ticket.setNumber(value);
            assertEquals(value, ticket.getNumber());
        }

        @ParameterizedTest
        @ValueSource(strings = {" ", "  ", "\t", "\n"})
        @DisplayName("accepts blank-whitespace numbers")
        void blankNumber(String value) {
            var ticket = new Ticket();
            ticket.setNumber(value);
            assertEquals(value, ticket.getNumber());
        }

        @Test
        @DisplayName("overwrites an existing number")
        void overwriteNumber() {
            var ticket = new Ticket(1, "OLD");
            ticket.setNumber("NEW");
            assertEquals("NEW", ticket.getNumber());
        }

        @Test
        @DisplayName("accepts long ticket number (boundary)")
        void longNumber() {
            String longNumber = "TK-" + "9".repeat(250);
            var ticket = new Ticket();
            ticket.setNumber(longNumber);
            assertEquals(longNumber, ticket.getNumber());
        }
    }

    @Nested
    @DisplayName("setPassenger / getPassenger")
    class PassengerAccessors {

        @Test
        @DisplayName("stores and returns a Passenger instance")
        void storesPassenger() {
            var passenger = new Passenger();
            var ticket    = new Ticket();

            ticket.setPassenger(passenger);

            assertSame(passenger, ticket.getPassenger());
        }

        @Test
        @DisplayName("allows overwriting passenger with another instance")
        void overwritePassenger() {
            var first  = new Passenger();
            var second = new Passenger();
            var ticket = new Ticket();

            ticket.setPassenger(first);
            ticket.setPassenger(second);

            assertSame(second, ticket.getPassenger());
        }

        @Test
        @DisplayName("allows setting passenger to null")
        void setPassengerNull() {
            var passenger = new Passenger();
            var ticket    = new Ticket();
            ticket.setPassenger(passenger);

            ticket.setPassenger(null);

            assertNull(ticket.getPassenger());
        }
    }

    // ---------------------------------------------------------------------------
    // State-combination tests
    // ---------------------------------------------------------------------------

    @Nested
    @DisplayName("State combination checks")
    class StateCombination {

        @Test
        @DisplayName("ticket with full data is consistent")
        void fullDataIsConsistent() {
            var passenger = new Passenger();
            var ticket    = new Ticket(7, "TK-007");
            ticket.setPassenger(passenger);

            Object obj = ticket;
            if (obj instanceof Ticket t) {
                assertAll(
                        () -> assertEquals(7,        t.getId()),
                        () -> assertEquals("TK-007", t.getNumber()),
                        () -> assertNotNull(t.getPassenger())
                );
            } else {
                fail("Object should be a Ticket instance");
            }
        }

        @Test
        @DisplayName("mutating id does not affect number or passenger")
        void mutatingIdDoesNotAffectOtherFields() {
            var passenger = new Passenger();
            var ticket    = new Ticket(1, "TK-100");
            ticket.setPassenger(passenger);

            ticket.setId(999);

            assertAll(
                    () -> assertEquals(999,      ticket.getId()),
                    () -> assertEquals("TK-100", ticket.getNumber()),
                    () -> assertSame(passenger,  ticket.getPassenger())
            );
        }

        @Test
        @DisplayName("mutating number does not affect id or passenger")
        void mutatingNumberDoesNotAffectOtherFields() {
            var passenger = new Passenger();
            var ticket    = new Ticket(1, "TK-100");
            ticket.setPassenger(passenger);

            ticket.setNumber("TK-200");

            assertAll(
                    () -> assertEquals(1,        ticket.getId()),
                    () -> assertEquals("TK-200", ticket.getNumber()),
                    () -> assertSame(passenger,  ticket.getPassenger())
            );
        }
    }
}