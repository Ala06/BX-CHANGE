package atlantafx.sampler.services;

public enum Currencies {
        BITCOIN("BTC", "Bitcoin"),
        ETHEREUM("ETH", "Ethereum"),
        RIPPLE("XRP", "Ripple"),
        LITECOIN("LTC", "Litecoin"),
        BITCOIN_CASH("BCH", "Bitcoin Cash"),
        CARDANO("ADA", "Cardano"),
        POLKADOT("DOT", "Polkadot"),
        BINANCE_COIN("BNB", "Binance Coin"),
        SOLANA("SOL", "Solana"),
        DOGECOIN("DOGE", "Dogecoin");

        private final String symbol;
        private final String fullName;

    Currencies(String symbol, String fullName) {
            this.symbol = symbol;
            this.fullName = fullName;
        }

    public String getSymbol() {
            return symbol;
        }

        public String getFullName() {
            return fullName;
        }

        @Override
        public String toString() {
            return fullName + " (" + symbol + ")";
        }


}
