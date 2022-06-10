package net.dongliu.apk.parser.struct.xml;

import net.dongliu.apk.parser.struct.ResourceValue;
import net.dongliu.apk.parser.struct.resource.ResourceTable;
import net.dongliu.apk.parser.utils.ResourceLoader;

import java.util.Locale;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * xml node attribute
 *
 * @author dongliu
 */
public class Attribute {
    private String namespace;
    private String name;
    // The original raw string value of Attribute
    private String rawValue;
    // Processed typed value of Attribute
    private ResourceValue typedValue;
    // the final value as string
    @Nullable
    private String value;

    @Nullable
    public String toStringValue(final ResourceTable resourceTable, final Locale locale) {
        final String rawValue = this.rawValue;
        if (rawValue != null) {
            return rawValue;
        } else {
            final ResourceValue typedValue = this.typedValue;
            if (typedValue != null) {
                return typedValue.toStringValue(resourceTable, locale);
            } else {
                // something happen;
                return "";
            }
        }
    }

    /**
     * These are attribute resource constants for the platform; as found in android.R.attr
     *
     * @author dongliu
     */
    public static class AttrIds {
        @NonNull
        private static final Map<Integer, String> ids = ResourceLoader.loadSystemAttrIds();

        @NonNull
        public static String getString(final long id) {
            String value = AttrIds.ids.get((int) id);
            if (value == null) {
                value = "AttrId:0x" + Long.toHexString(id);
            }
            return value;
        }

    }

    public String getNamespace() {
        return this.namespace;
    }

    public void setNamespace(final String namespace) {
        this.namespace = namespace;
    }

    public String getName() {
        return this.name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getRawValue() {
        return this.rawValue;
    }

    public void setRawValue(final String rawValue) {
        this.rawValue = rawValue;
    }

    public ResourceValue getTypedValue() {
        return this.typedValue;
    }

    public void setTypedValue(final @Nullable ResourceValue typedValue) {
        this.typedValue = typedValue;
    }

    @Nullable
    public String getValue() {
        return this.value;
    }

    public void setValue(final @Nullable String value) {
        this.value = value;
    }

    @NonNull
    @Override
    public String toString() {
        return "Attribute{" +
                "name='" + this.name + '\'' +
                ", namespace='" + this.namespace + '\'' +
                '}';
    }
}
