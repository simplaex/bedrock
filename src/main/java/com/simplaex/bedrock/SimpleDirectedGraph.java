package com.simplaex.bedrock;

import lombok.Getter;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;
import java.util.function.IntConsumer;

/**
 * A simple directed graph is defined by it's vertices and edges, where the vertices
 * are simple indices starting from zero up to the number of vertices.
 */
public class SimpleDirectedGraph {

  @Getter
  final int numberOfVertices;

  @Getter
  final int numberOfEdges;

  private final int[][] outgoingEdges;

  private int[][] incomingEdges = null;

  SimpleDirectedGraph(
    final int numberOfVertices,
    final int numberOfEdges,
    final int[][] outgoingEdges,
    @Nullable final int[][] incomingEdges
  ) {

    this.numberOfVertices = numberOfVertices;
    this.numberOfEdges = numberOfEdges;
    this.outgoingEdges = outgoingEdges;
    this.incomingEdges = incomingEdges;
  }

  public final void forEachOutgoing(final int index, final IntConsumer consumer) {
    final int[] outgoing = outgoingEdges[index];
    for (int i = 0; i < outgoing.length; i += 1) {
      consumer.accept(outgoing[i]);
    }
  }

  public final int countOutgoing(final int index) {
    return outgoingEdges[index].length;
  }

  public final boolean hasOutgoing(final int index) {
    return countIncoming(index) > 0;
  }

  public final void forEachIncoming(final int index, final IntConsumer consumer) {
    checkIncoming();
    final int[] incoming = incomingEdges[index];
    for (int i = 0; i < incoming.length; i += 1) {
      consumer.accept(incoming[i]);
    }
  }

  public final int countIncoming(final int index) {
    checkIncoming();
    return incomingEdges[index].length;
  }

  public final boolean hasIncoming(final int index) {
    return countIncoming(index) > 0;
  }

  private void checkIncoming() {
    if (incomingEdges == null) {
      incomingEdges = calculateIncoming();
    }
  }

  private int[][] calculateIncoming() {
    final Map<Integer, TreeSet<Integer>> incomingEdgesMap = new HashMap<>();
    for (int i = 0; i < numberOfVertices; i += 1) {
      incomingEdgesMap.put(i, new TreeSet<>());
    }
    for (int i = 0; i < outgoingEdges.length; i += 1) {
      final int[] out = outgoingEdges[i];
      for (int j = 0; j < out.length; j += 1) {
        final int from = i;
        final int to = out[j];
        incomingEdgesMap.get(to).add(from);
      }
    }
    final int[][] incomingEdges = new int[numberOfVertices][];
    incomingEdgesMap.forEach((to, froms) -> {
      final int[] in = new int[froms.size()];
      int i = 0;
      for (final int from : incomingEdgesMap.get(to)) {
        in[i++] = from;
      }
      incomingEdges[to] = in;
    });
    return incomingEdges;
  }

}
