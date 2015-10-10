package de.jungblut.nlp.mr;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.WritableComparable;

import com.google.common.collect.ComparisonChain;

public final class TextIntPairWritable implements
    WritableComparable<TextIntPairWritable> {

  private Text first;
  private IntWritable second;

  public TextIntPairWritable() {
  }

  public TextIntPairWritable(Text first, IntWritable second) {
    this.first = first;
    this.second = second;
  }

  @Override
  public void readFields(DataInput in) throws IOException {
    first = new Text();
    first.readFields(in);
    second = new IntWritable();
    second.readFields(in);
  }

  @Override
  public void write(DataOutput out) throws IOException {
    first.write(out);
    second.write(out);
  }

  @Override
  public int compareTo(TextIntPairWritable o) {
    return ComparisonChain.start().compare(first, o.first)
        .compare(second, o.second).result();
  }

  public Text getFirst() {
    return first;
  }

  public IntWritable getSecond() {
    return second;
  }

  @Override
  public String toString() {
    return "Pair [" + (first != null ? "first=" + first + ", " : "")
        + (second != null ? "second=" + second : "") + "]";
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((first == null) ? 0 : first.hashCode());
    result = prime * result + ((second == null) ? 0 : second.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    TextIntPairWritable other = (TextIntPairWritable) obj;
    if (first == null) {
      if (other.first != null) {
        return false;
      }
    } else if (!first.equals(other.first)) {
      return false;
    }
    if (second == null) {
      if (other.second != null) {
        return false;
      }
    } else if (!second.equals(other.second)) {
      return false;
    }
    return true;
  }

}
