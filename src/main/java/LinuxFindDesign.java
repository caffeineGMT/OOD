// Q: Implement find command as an API (similar to Linux find command).
// I will give you a base directory and the API will support finding files by size and name.
// Create a library that lets me do this easily. Keep in mind that these are just 2 uses cases and that the library should be flexible.

// Sample rules:
// * Find all files over XX MB somewhere under a directory.
// * Find all XML files somewhere under a directory.

// sample :
// % find / -name "*.xml" -size +1k
// % find / -name "*.png" -size -1m
// % find / -size +100m
// % find / -name "temp-*"

// ref: https://leetcode.com/playground/fsCyWvAw
// ref: https://github.com/ermalsh/coding-interview-examples/blob/master/Find.java

import java.sql.Timestamp;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class LinuxFindDesign {
    class File {
        String name;
        String extension;
        int size;
        boolean isDir;
        List<File> children;
        Timestamp deleted;
        Timestamp created;
        Timestamp modified;

        File() {
            name = "";
            extension = "";
            size = 0;
            isDir = false;
            children = new ArrayList<>();
        }
    }

    File root = new File();

    class SearchParams {
        String extension;
        Integer minSize;
        Integer maxSize;
        String name;
    }

    interface Filter {
        boolean isValid(SearchParams params, File file);
    }

    class ExtensionFilter implements Filter {
        @Override
        public boolean isValid(SearchParams params, File file) {
            if (params.extension == null) return true;
            return file.extension.equals(params.extension);
        }
    }

    class MinSizeFilter implements Filter {
        @Override
        public boolean isValid(SearchParams params, File file) {
            if (params.minSize == null) return true;
            return file.size >= params.minSize;
        }
    }

    class MaxSizeFilter implements Filter {
        @Override
        public boolean isValid(SearchParams params, File file) {
            if (params.maxSize == null) return true;
            return file.size <= params.maxSize;
        }
    }

    class NameFilter implements Filter {
        @Override
        public boolean isValid(SearchParams params, File file) {
            if (params.name == null) return true;
            return file.name.equals(params.name);
        }
    }

    class FileFilter {
        List<Filter> filters;
        FileFilter() {
            filters = new ArrayList<>();
            filters.add(new ExtensionFilter());
            filters.add(new NameFilter());
            filters.add(new MaxSizeFilter());
            filters.add(new MinSizeFilter());
        }
        public boolean isValid(SearchParams params, File file) {
            for (Filter filter: filters) {
                if (!filter.isValid(params, file)) {
                    return false;
                }
            }
            return true;
        }
    }

    class FileSearcher {
        private FileFilter fileFilter;
        FileSearcher() {
            FileFilter fileFilter= new FileFilter();
        }

        public List<File> search(File root, SearchParams searchParams) {
            List<File> res = new ArrayList<>();
            Queue<File> q = new ArrayDeque<>();
            q.offer(root);
            while(!q.isEmpty()) {
                File file = q.poll();
                if (!file.isDir && fileFilter.isValid(searchParams, file)) {
                    res.add(file);
                    continue;
                }
                for (File neighbor: file.children) {
                    q.offer(neighbor);
                }
            }
            return res;
        }
    }
}
