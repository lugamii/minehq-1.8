package dev.lugami.hqueue.data;

import java.util.Comparator;

public class QueueComparator
implements Comparator<QueuePlayer> {
    @Override
    public int compare(QueuePlayer firstPlayer, QueuePlayer secondPlayer) {
        return secondPlayer.compareTo(firstPlayer);
    }
}

