package storage;

public class Data {
        String data;
        long expiryTime;

        public Data(String val, long expiryTime) {
            this.data = val;
            this.expiryTime = expiryTime;
        }

        public String getData() {
            return data;
        }

        public long getExpiryTime() {
            return expiryTime;
        }
    }