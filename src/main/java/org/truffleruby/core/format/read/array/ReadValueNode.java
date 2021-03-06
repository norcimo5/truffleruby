/*
 * Copyright (c) 2015, 2017 Oracle and/or its affiliates. All rights reserved. This
 * code is released under a tri EPL/GPL/LGPL license. You can use it,
 * redistribute it and/or modify it under the terms of the:
 *
 * Eclipse Public License version 1.0, or
 * GNU General Public License version 2, or
 * GNU Lesser General Public License version 2.1.
 */
package org.truffleruby.core.format.read.array;

import com.oracle.truffle.api.dsl.Cached;
import com.oracle.truffle.api.dsl.ImportStatic;
import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.NodeChildren;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.VirtualFrame;

import org.truffleruby.core.array.ArrayGuards;
import org.truffleruby.core.array.ArrayOperationNodes;
import org.truffleruby.core.array.ArrayStrategy;
import org.truffleruby.core.format.FormatNode;
import org.truffleruby.core.format.read.SourceNode;

@NodeChildren({
        @NodeChild(value = "source", type = SourceNode.class),
})
@ImportStatic(ArrayGuards.class)
public abstract class ReadValueNode extends FormatNode {

    @Specialization(guards = "isNull(source)")
    public void read(VirtualFrame frame, Object source) {
        advanceSourcePosition(frame);
        throw new IllegalStateException();
    }

    @Specialization
    public long read(VirtualFrame frame, int[] source) {
        return source[advanceSourcePosition(frame)];
    }

    @Specialization
    public long read(VirtualFrame frame, long[] source) {
        return source[advanceSourcePosition(frame)];
    }

    @Specialization
    public long read(VirtualFrame frame, double[] source) {
        return (long) source[advanceSourcePosition(frame)];
    }

    @Specialization(guards = "strategy.matchesStore(source)", limit = "STORAGE_STRATEGIES")
    public Object read(VirtualFrame frame, Object source,
            @Cached("ofStore(source)") ArrayStrategy strategy,
            @Cached("strategy.getNode()") ArrayOperationNodes.ArrayGetNode getNode) {
        return getNode.execute(source, advanceSourcePosition(frame));
    }

}
