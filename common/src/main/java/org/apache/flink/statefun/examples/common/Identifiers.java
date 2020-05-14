package org.apache.flink.statefun.examples.common;

import org.apache.flink.statefun.sdk.FunctionType;
import org.apache.flink.statefun.sdk.io.EgressIdentifier;
import org.apache.flink.statefun.sdk.io.IngressIdentifier;

public class Identifiers {

    public static final FunctionType MAPPER_TYPE = new FunctionType("demo", "mapper");
    public static final FunctionType GREETER_TYPE = new FunctionType("demo", "greeter");

    public static final IngressIdentifier<String> INBOUND_IDENTIFIER =
        new IngressIdentifier<>(String.class, "demo", "inbound-id");

    public static final EgressIdentifier<String> OUTBOUND_IDENTIFIER =
        new EgressIdentifier<>("demo", "outbound-id", String.class);

}
