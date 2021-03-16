package queue;

public abstract class AbstractQueue implements Queue {
    protected int size;

    @Override
    public final void enqueue(final Object e) {
        assert null != e;
        enqueueImpl(e);
        ++size;
    }

    @Override
    public final Object element() {
        assert !isEmpty();
        return elementImpl();
    }

    @Override
    public final Object dequeue() {
        assert !isEmpty();
        final var e = dequeueImpl();
        --size;
        return e;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return 0 == size;
    }

    @Override
    public final void clear() {
        if (0 != size) {
            clearImpl();
            size = 0;
        }
    }

    @Override
    public boolean contains(final Object e) {
        return containsAndRemoveIfNeeded(e, false);
    }

    @Override
    public boolean removeFirstOccurrence(final Object e) {
        return containsAndRemoveIfNeeded(e, true);
    }

    protected boolean containsAndRemoveIfNeeded(final Object e, final boolean remove) {
        var found = false;
        final var size = this.size;

        for (var i = 0; i < size; ++i) {
            final var element = dequeue();
            if (!found && element.equals(e)) {
                found = true;
                if (remove) {
                    continue;
                }
            }
            enqueue(element);
        }
        return found;
    }

    protected abstract void enqueueImpl(final Object e);

    protected abstract Object elementImpl();

    protected abstract Object dequeueImpl();

    protected abstract void clearImpl();
}
