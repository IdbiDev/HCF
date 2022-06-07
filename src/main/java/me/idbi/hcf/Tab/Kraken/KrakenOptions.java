package me.idbi.hcf.Tab.Kraken;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(fluent = true, chain = true)
public class KrakenOptions {

    private boolean sendCreationMessage;

    public static KrakenOptions getDefaultOptions() {
        return new KrakenOptions().sendCreationMessage(true);
    }
}
