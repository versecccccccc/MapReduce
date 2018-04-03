import java.io.IOException;  
import java.util.*;  
import java.math.BigDecimal; 
  
import org.apache.hadoop.conf.Configuration;  
import org.apache.hadoop.fs.Path;  
import org.apache.hadoop.io.IntWritable;  
import org.apache.hadoop.io.LongWritable;  
import org.apache.hadoop.io.Text;  
import org.apache.hadoop.mapreduce.Job;  
import org.apache.hadoop.mapreduce.Mapper;  
import org.apache.hadoop.mapreduce.Reducer;  
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;  
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;  
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;  
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;  
import org.apache.hadoop.mapreduce.lib.partition.HashPartitioner;  
import org.apache.commons.lang.StringUtils;
 
  
public class PageRank {  
    
    static enum PageCount{
        Count, TotalPR
    }

    public static class PageRankMap extends  
            Mapper<LongWritable, Text, Text, Text> {  
        public void map(LongWritable key, Text value, Context context)  
                throws IOException, InterruptedException {  
            String[] kv = value.toString().split(" ");
            String ky = kv[0];
            String pr = kv[kv.length - 1];
            String[] link = new String[10];
            for(int i = 0; i < kv.length-1; i++)
                link[i] = kv[i];
            String j = StringUtils.join(link, " ");
            context.write(new Text(ky), new Text(j));
            float score = Float.valueOf(pr)/(kv.length-2) * 1.0f;
            for(int i = 1; i < kv.length - 1; i++){
                context.write(new Text(kv[i]), new Text(String.valueOf(score)));
            }
        }  
    }  
  
    public static class PageRankReduce extends  
            Reducer<Text, Text, Text, Text> {  
        public void reduce(Text key, Iterable<Text> values,  
                Context context) throws IOException, InterruptedException {  
            StringBuilder sb = new StringBuilder();
            float pr = 0f;
            for(Text val : values){
                String value = val.toString();
                int s = value.indexOf(".");
                if(s != -1){
                    pr += Float.valueOf(value);
                }
                else{
                    String site[] = value.split(" ");
                    int len = site.length;
                    for(int j = 0; j < len; j ++){
                        sb.append(site[j]);
                        sb.append(" ");
                    }
                }
            }
            String output = sb.toString() + " " + pr;
            context.write(new Text(output),new Text());
        }  
    }  
  
    public static void main(String[] args) throws Exception {  
        Configuration conf = new Configuration();  
        Job job = new Job(conf);  
        job.setJarByClass(PageRank.class);  
        job.setJobName("pagerank");  
  
        job.setOutputKeyClass(Text.class);  
        job.setOutputValueClass(Text.class);  
  
        job.setMapperClass(PageRankMap.class);  
        job.setReducerClass(PageRankReduce.class);  
  
        job.setInputFormatClass(TextInputFormat.class);  
        job.setOutputFormatClass(TextOutputFormat.class);  
  
        FileInputFormat.addInputPath(job, new Path(args[0]));  
        FileOutputFormat.setOutputPath(job, new Path(args[1]));  
  
        job.waitForCompletion(true);  
    }  
}  
