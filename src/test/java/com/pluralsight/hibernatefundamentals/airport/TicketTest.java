package com.pluralsight.hibernatefundamentals.airport;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Ticket entity tests")
class TicketTest {

    // -------------------------------------------------------------------------
    // Construction
    // -------------------------------------------------------------------------

    @Nested
    @DisplayName("Constructor tests")
    class ConstructorTests {

        @Test
        @DisplayName("No-arg constructor creates ticket with default values")
        void noArgConstructorCreatesTicketWithDefaultValues() {
            var ticket = new Ticket();

            assertThat(ticket.getId()).isZero();
            assertThat(ticket.getNumber()).isNull();
            assertThat(ticket.getPassenger()).isNull();
        }

        @Test
        @DisplayName("Parameterized constructor sets id and number correctly")
        void parameterizedConstructorSetsIdAndNumber() {
            var ticket = new Ticket(42, "TKT-001");

            assertThat(ticket.getId()).isEqualTo(42);
            assertThat(ticket.getNumber()).isEqualTo("TKT-001");
            assertThat(ticket.getPassenger()).isNull();
        }

        @Test
        @DisplayName("Parameterized constructor with zero id")
        void parameterizedConstructorWithZeroId() {
            var ticket = new Ticket(0, "TKT-ZERO");

            assertThat(ticket.getId()).isZero();
            assertThat(ticket.getNumber()).isEqualTo("TKT-ZERO");
        }

        @Test
        @DisplayName("Parameterized constructor with negative id stores value as-is")
        void parameterizedConstructorWithNegativeId() {
            var ticket = new Ticket(-1, "TKT-NEG");

            assertThat(ticket.getId()).isEqualTo(-1);
        }

        @Test
        @DisplayName("Parameterized constructor with null number")
        void parameterizedConstructorWithNullNumber() {
            var ticket = new Ticket(1, null);

            assertThat(ticket.getId()).isEqualTo(1);
            assertThat(ticket.getNumber()).isNull();
        }
    }

    // -------------------------------------------------------------------------
    // getId / setId
    // -------------------------------------------------------------------------

    @Nested
    @DisplayName("id field tests")
    class IdTests {

        @Test
        @DisplayName("setId updates id returned by getId")
        void setIdUpdatesId() {
            var ticket = new Ticket();
            ticket.setId(99);

            assertThat(ticket.getId()).isEqualTo(99);
        }

        @Test
        @DisplayName("setId can be called multiple times; last value wins")
        void setIdMultipleTimes() {
            var ticket = new Ticket(1, "TKT-001");
            ticket.setId(100);
            ticket.setId(200);

            assertThat(ticket.getId()).isEqualTo(200);
        }

        @ParameterizedTest(name = "id={0}")
        @ValueSource(ints = {0, 1, Integer.MAX_VALUE, Integer.MIN_VALUE})
        @DisplayName("setId accepts boundary integer values")
        void setIdBoundaryValues(int id) {
            var ticket = new Ticket();
            ticket.setId(id);

            assertThat(ticket.getId()).isEqualTo(id);
        }
    }

    // -------------------------------------------------------------------------
    // getNumber / setNumber
    // -------------------------------------------------------------------------

    @Nested
    @DisplayName("number field tests")
    class NumberTests {

        @Test
        @DisplayName("setNumber updates number returned by getNumber")
        void setNumberUpdatesNumber() {
            var ticket = new Ticket();
            ticket.setNumber("ABC-123");

            assertThat(ticket.getNumber()).isEqualTo("ABC-123");
        }

        @Test
        @DisplayName("setNumber can overwrite existing value")
        void setNumberOverwritesExistingValue() {
            var ticket = new Ticket(1, "OLD-NUMBER");
            ticket.setNumber("NEW-NUMBER");

            assertThat(ticket.getNumber()).isEqualTo("NEW-NUMBER");
        }

        @ParameterizedTest
        @NullAndEmptySource
        @DisplayName("setNumber accepts null and empty string")
        void setNumberNullAndEmpty(String number) {
            var ticket = new Ticket();
            ticket.setNumber(number);

            assertThat(ticket.getNumber()).isEqualTo(number);
        }

        @Test
        @DisplayName("setNumber with a long ticket number (text block usage)")
        void setNumberWithLongValue() {
            var longNumber = """
                    VERY-LONG-TICKET-NUMBER-0000000001""".strip();

            var ticket = new Ticket();
            ticket.setNumber(longNumber);

            assertThat(ticket.getNumber()).isEqualTo("VERY-LONG-TICKET-NUMBER-0000000001");
        }

        @Test
        @DisplayName("setNumber with whitespace-only string")
        void setNumberWhitespaceOnly() {
            var ticket = new Ticket();
            ticket.setNumber("   ");

            assertThat(ticket.getNumber()).isEqualTo("   ");
        }
    }

    // -------------------------------------------------------------------------
    // getPassenger / setPassenger
    // -------------------------------------------------------------------------

    @Nested
    @DisplayName("passenger relationship tests")
    class PassengerTests {

        @Test
        @DisplayName("getPassenger returns null when not set")
        void getPassengerReturnsNullWhenNotSet() {
            var ticket = new Ticket(1, "TKT-001");

            assertThat(ticket.getPassenger()).isNull();
        }

        @Test
        @DisplayName("setPassenger stores and retrieves the passenger")
        void setPassengerStoresPassenger() {
            var passenger = new Passenger();
            var ticket = new Ticket(1, "TKT-001");

            ticket.setPassenger(passenger);

            assertThat(ticket.getPassenger()).isSameAs(passenger);
        }

        @Test
        @DisplayName("setPassenger can be set to null explicitly")
        void setPassengerToNull() {
            var passenger = new Passenger();
            var ticket = new Ticket(1, "TKT-001");
            ticket.setPassenger(passenger);

            ticket.setPassenger(null);

            assertThat(ticket.getPassenger()).isNull();
        }

        @Test
        @DisplayName("setPassenger can be replaced with a different passenger")
        void setPassengerCanBeReplaced() {
            var firstPassenger = new Passenger();
            var secondPassenger = new Passenger();
            var ticket = new Ticket(1, "TKT-001");

            ticket.setPassenger(firstPassenger);
            ticket.setPassenger(secondPassenger);

            assertThat(ticket.getPassenger()).isSameAs(secondPassenger);
            assertThat(ticket.getPassenger()).isNotSameAs(firstPassenger);
        }
    }

    // -------------------------------------------------------------------------
    // Combined / integration-style scenarios
    // -------------------------------------------------------------------------

    @Nested
    @DisplayName("Combined field mutation scenarios")
    class CombinedTests {

        @Test
        @DisplayName("Full round-trip: construct, mutate all fields, verify")
        void fullRoundTrip() {
            // Arrange via constructor
            var ticket = new Ticket(1, "INIT");
            var passenger = new Passenger();

            // Act
            ticket.setId(7);
            ticket.setNumber("TKT-FINAL");
            ticket.setPassenger(passenger);

            // Assert – using pattern matching instanceof check (Java 16+)
            assertThat(ticket.getId()).isEqualTo(7);
            assertThat(ticket.getNumber()).isEqualTo("TKT-FINAL");
            assertThat(ticket.getPassenger() instanceof Passenger).isTrue();
            assertThat(ticket.getPassenger()).isSameAs(passenger);
        }

        @Test
        @DisplayName("Two tickets with same data are independent objects")
        void twoTicketsWithSameDataAreIndependent() {
            var t1 = new Ticket(1, "TKT-001");
            var t2 = new Ticket(1, "TKT-001");

            assertThat(t1).isNotSameAs(t2);
            assertThat(t1.getId()).isEqualTo(t2.getId());
            assertThat(t1.getNumber()).isEqualTo(t2.getNumber());
        }

        @Test
        @DisplayName("Mutating one ticket does not affect another")
        void mutatingOneTicketDoesNotAffectAnother() {
            var t1 = new Ticket(1, "TKT-001");
            var t2 = new Ticket(1, "TKT-001");

            t1.setNumber("CHANGED");

            assertThat(t2.getNumber()).isEqualTo("TKT-001");
        }
    }
}
