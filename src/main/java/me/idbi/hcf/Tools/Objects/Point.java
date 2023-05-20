package me.idbi.hcf.Tools.Objects;

import lombok.Getter;
import lombok.Setter;

public class Point {
        @Getter
        @Setter
        private int x, z;

        public Point(int x, int z) {
            this.x = x;
            this.z = z;
        }

}
