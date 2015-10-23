/*
 * Copyright (c) 2014, Oracle America, Inc.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  * Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 *  * Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 *
 *  * Neither the name of Oracle nor the names of its contributors may be used
 *    to endorse or promote products derived from this software without
 *    specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
 * THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.podbox.labs;

import com.google.common.collect.Lists;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.infra.Blackhole;

import java.util.*;

@State(Scope.Thread)
public class MyBenchmark {

    private ArrayList<String> list;
    private ArrayDeque<String> deque;
    private Stack<String> stack;

    @Setup
    public void init() {
        this.deque = new ArrayDeque<String>();
        this.stack = new Stack<>();
        this.list = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            this.deque.add(String.valueOf(i));
            this.list.add(String.valueOf(i));
            this.stack.add(String.valueOf(i));
        }
    }

    @Benchmark
    public void testStack(Blackhole bh) {

        boolean found = false;
        Iterator<String> it = this.stack.iterator();
        while (it.hasNext() && !found) {
            String s = it.next();
            found = "40".equals(s);
        }
        bh.consume(found);
    }

    @Benchmark
    public void testDeque(Blackhole bh) {

        boolean found = false;
        Iterator<String> it = this.deque.descendingIterator();
        while (!found && it.hasNext()) {
            String s = it.next();
            found = "40".equals(s);
        }
        bh.consume(found);
    }

    @Benchmark
    public void testGuava(Blackhole bh) {

        boolean found = false;
        Iterator<String> reverseIt = Lists.reverse(this.list).iterator();
        while (reverseIt.hasNext() && !found) {
            String s = reverseIt.next();
            found = "40".equals(s);
        }
        bh.consume(found);
    }

    @Benchmark
    public void testPingPong(Blackhole bh) {

        boolean found = false;
        ListIterator<String> lIterator = list.listIterator();
        while (lIterator.hasNext()) lIterator.next();

        while (!found && lIterator.hasPrevious()) {
            String s = lIterator.previous();
            found = "40".equals(s);
        }
        bh.consume(found);
    }

    @Benchmark
    public void testListIteratorRev(Blackhole bh) {

        boolean found = false;
        ListIterator<String> lIterator = list.listIterator(list.size());
        while (!found && lIterator.hasPrevious()) {
            String s = lIterator.previous();
            found = "40".equals(s);
        }
        bh.consume(found);
    }

    @Benchmark
    public void testCollectionReverse(Blackhole bh) {

        boolean found = false;
        List<String> reverse = (List<String>) list.clone();
        Collections.reverse(reverse);
        Iterator<String> it = reverse.iterator();
        while (!found && it.hasNext()) {
            String s = it.next();
            found = "40".equals(s);
        }
        bh.consume(found);
    }

    @Benchmark
    public void testFromBeginning(Blackhole bh) {

        boolean found = false;
        Iterator<String> it = list.iterator();
        while (!found && it.hasNext()) {
            String s = it.next();
            found = "40".equals(s);
        }

        bh.consume(found);
    }

    @Benchmark
    public void testRegularLoop(Blackhole bh) {

        String res = null;
        for (String s : list) {
            if ("40".equals(s)) {
                res = s;
            }
        }
        bh.consume(res);
    }

    @Benchmark
    public void testStream(Blackhole bh) {

        Optional<String> res = list.stream().filter(s -> "40".equals(s)).findFirst();
        bh.consume(res);
    }
}
