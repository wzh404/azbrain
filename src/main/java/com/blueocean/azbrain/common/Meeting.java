package com.blueocean.azbrain.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.TreeSet;

@Data
public class Meeting {
    private String host;
    private String pwd;

    @JsonIgnore
    private TreeSet<TimeNode> nodes;

    public Meeting(String host, String pwd) {
        this.host = host;
        this.pwd = pwd;

        nodes = new TreeSet<>();
    }

    public boolean add(LocalDateTime s, LocalDateTime e) {
        if (s.isAfter(e)) return false;

        nodes.removeIf(TimeNode::expired);
        return nodes.add(new TimeNode(s, e));
    }

    public boolean contains(LocalDateTime s, LocalDateTime e) {
        return nodes.contains(new TimeNode(s, e));
    }

    public void remove(LocalDateTime s, LocalDateTime e) {
        nodes.removeIf(n -> (n.getStartTime().compareTo(s) == 0 && n.getEndTime().compareTo(e) == 0));
    }

    @Data
    public class TimeNode implements Comparable<TimeNode> {
        private LocalDateTime startTime;
        private LocalDateTime endTime;

        public TimeNode(LocalDateTime s, LocalDateTime e) {
            this.startTime = s;
            this.endTime = e;
        }

        /**
         * 与指定的节点时间是否重叠
         *
         * @param t
         * @return
         */
        public boolean overlap(TimeNode t) {
            return !((t.startTime.isAfter(this.endTime) ||
                    t.startTime.isBefore(this.startTime)) &&
                    (t.endTime.isAfter(this.endTime) ||
                            t.endTime.isBefore(this.startTime)));
        }

        /**
         * 节点是否过期
         *
         * @return
         */
        public boolean expired() {
            return endTime.isBefore(LocalDateTime.now());
        }

        @Override
        public int compareTo(TimeNode o) {
            if (overlap(o)) {
                return 0;
            }

            if (o.endTime.isBefore(this.startTime)) {
                return -1;
            } else {
                return 1;
            }
        }

        @Override
        public boolean equals(Object n) {
            if (this == n) {
                return true;
            }

            if (n == null || getClass() != n.getClass()) {
                return false;
            }

            return overlap((TimeNode) n);
        }
    }
}
