package cn.wzbos.android.rudolph;

import java.util.LinkedHashMap;
import java.util.Map;


/**
 * Route Meta
 */
public class RouteInfo {
    private String path;
    private String target;
    private Map<String, String> params;
    private RouteType routeType;
    private String tag;

    public String getTag() {
        return tag;
    }

    public String getTarget() {
        return target;
    }

    public RouteType getRouteType() {
        return routeType;
    }

    public String getPath() {
        return path;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setType(RouteType routeType) {
        this.routeType = routeType;
    }

    public RouteType getType() {
        return routeType;
    }

    private RouteInfo(Builder builder) {
        this.path = builder.path;
        this.target = builder.target;
        this.routeType = builder.routeType;
        this.params = builder.params;
        this.tag = builder.tag;
    }

    /**
     * 比较路由地址
     */
    boolean equals(String currPath) {
        //path params
        String[] segments = path.split("/");

        String[] currSegments = currPath.split("/");

        //segments
        if (segments.length != currSegments.length) {
            return false;
        }

        for (int i = 0; i < segments.length; i++) {
            if (segments[i].startsWith(":"))
                continue;

            if (!segments[i].equalsIgnoreCase(currSegments[i]))
                return false;
        }
        return true;
    }

    public static class Builder {
        private String path;
        private String target;
        private Map<String, String> params;
        private RouteType routeType;
        private String tag;

        public Builder path(String path) {
            this.path = path;
            return this;
        }

        public Builder target(String target) {
            this.target = target;
            return this;
        }

        public Builder putParam(String key, String type) {
            if (this.params == null) {
                this.params = new LinkedHashMap<>();
            }
            this.params.put(key, type);
            return this;
        }

        public Builder routeType(RouteType routeType) {
            this.routeType = routeType;
            return this;
        }

        public Builder tag(String tag) {
            this.tag = tag;
            return this;
        }

        public RouteInfo build() {
            return new RouteInfo(this);
        }
    }

}