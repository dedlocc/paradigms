package queue;

import java.util.Collections;
import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.Supplier;

public final class Main {
    private final static Object[] ELEMENTS = new Object[] {
        -1, 0, 1,
        Double.NaN,
        "Hello World", "的",
        new String[] {"x", "y", "z"},
        List.of("Lorem", "ipsum", "dolor", "sit", "amet"),
        Collections.emptyMap(),
    };

    public static void main(final String[] args) {
        System.out.println("======= MODULE =======");
        testModule();
        System.out.println("======= ADT =======");
        testADT();
        System.out.println("======= CLASS =======");
        testClass();
    }


    private static void testModule() {
        System.out.println("Queue:");

        fill(ArrayQueueModule::enqueue);
        dump(ArrayQueueModule::dequeue, ArrayQueueModule::isEmpty);

        System.out.println("Stack:");

        fill(ArrayQueueModule::enqueue);
        dump(ArrayQueueModule::remove, ArrayQueueModule::isEmpty);

        System.out.println("Clear:");
        fill(ArrayQueueModule::push);
        System.out.println("size: " + ArrayQueueModule.size());
        ArrayQueueModule.clear();
        System.out.println("size: " + ArrayQueueModule.size());
        dump(ArrayQueueModule::remove, ArrayQueueModule::isEmpty);
    }

    private static void testADT() {
        final ArrayQueueADT q1 = ArrayQueueADT.create(), q2 = ArrayQueueADT.create();

        fill(e -> ArrayQueueADT.enqueue(q1, e));
        fill(e -> ArrayQueueADT.enqueue(q2, e));

        System.out.println("Queue:");
        dump(() -> ArrayQueueADT.dequeue(q1), () -> ArrayQueueADT.isEmpty(q1));

        System.out.println("Stack:");
        dump(() -> ArrayQueueADT.remove(q2), () -> ArrayQueueADT.isEmpty(q2));

        System.out.println("Clear:");
        fill(e -> ArrayQueueADT.enqueue(q1, e));
        System.out.println("size: " + ArrayQueueADT.size(q1));
        ArrayQueueADT.clear(q1);
        System.out.println("size: " + ArrayQueueADT.size(q1));
        dump(() -> ArrayQueueADT.dequeue(q1), () -> ArrayQueueADT.isEmpty(q1));
    }

    private static void testClass() {
        final ArrayQueue q1 = new ArrayQueue(), q2 = new ArrayQueue();

        fill(q1::enqueue);
        fill(q2::enqueue);

        System.out.println("Queue:");
        dump(q1::dequeue, q1::isEmpty);

        System.out.println("Stack:");
        dump(q2::remove, q2::isEmpty);

        System.out.println("Clear:");
        fill(q1::push);
        System.out.println("size: " + q1.size());
        q1.clear();
        System.out.println("size: " + q1.size());
        dump(q1::remove, q1::isEmpty);
    }

    private static void fill(final Consumer<Object> push) {
        for (final var e : ELEMENTS) {
            push.accept(e);
        }
    }

    private static void dump(final Supplier<Object> pop, final BooleanSupplier isEmpty) {
        while (!isEmpty.getAsBoolean()) {
            System.out.println("\t" + pop.get());
        }
    }
}
