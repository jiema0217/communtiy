package io.github.jiema.communtiy.cache;

import io.github.jiema.communtiy.dto.TagDTO;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TagCache {
    /**
     * 便于查询其他相似的问题，对tag进行严格的管理
     * 把tag全部标签缓存在这里
     * @return tagDTOS
     */
    public static List<TagDTO> get() {
        List<TagDTO> tagDTOS = new ArrayList<>();
        TagDTO program = new TagDTO();
        program.setCategoryName("开发语言");
        program.setTags(Arrays.asList("js", "php", "css", "html", "java", "node.js", "python", "c", "c++", "c#", "shell", "golang", "swift"));
        tagDTOS.add(program);

        TagDTO framework = new TagDTO();
        framework.setCategoryName("平台框架");
        framework.setTags(Arrays.asList("spring", "koa", "struts", "tornado", "spring boot", "spring cloud", "django", ".net"));
        tagDTOS.add(framework);

        TagDTO server = new TagDTO();
        server.setCategoryName("服务器");
        server.setTags(Arrays.asList("tomcat", "apache", "linux", "ubuntu", "centos", "unix", "docker", "缓存", "负载均衡", "windows-server"));
        tagDTOS.add(server);

        TagDTO db = new TagDTO();
        db.setCategoryName("数据库");
        db.setTags(Arrays.asList("mysql", "oracle", "mongodb", "redis", "sql", "h2", "sqlserver"));
        tagDTOS.add(db);

        TagDTO tool = new TagDTO();
        tool.setCategoryName("开发工具");
        tool.setTags(Arrays.asList("git", "github", "vim", "idea", "maven", "atom", "vscode", "visual-studio", "eclipse"));
        tagDTOS.add(tool);
        return tagDTOS;
    }

    /**
     * 用户可能会输入非法的tag，不便于查找相似问题
     * 对其进行校验，返回给用户不合法的tag
     * @param tags
     * @return invalid
     */
    public static String filterInvalid(String tags) {
        String[] split = StringUtils.split(tags, ",");
        List<TagDTO> tagDTOS = get();
        List<String> tagList = tagDTOS.stream().flatMap(tag -> tag.getTags().stream()).collect(Collectors.toList());
        String invalid = Arrays.stream(split).filter(t -> !tagList.contains(t)).collect(Collectors.joining(","));
        return invalid;
    }
}
