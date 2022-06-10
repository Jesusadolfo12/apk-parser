package net.dongliu.apk.parser.struct.xml;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * @author dongliu
 */
public class XmlNamespaceStartTag {
    @Nullable
    public final String prefix;
    @Nullable
    public final String uri;

    public XmlNamespaceStartTag(final @Nullable String prefix, final @Nullable String uri) {
        this.prefix = prefix;
        this.uri = uri;
    }


    @NonNull
    @Override
    public String toString() {
        return this.prefix + "=" + this.uri;
    }
}
