package com.metabubble.BWC.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.metabubble.BWC.common.R;
import com.metabubble.BWC.dto.Imp.MerchantConverter;
import com.metabubble.BWC.dto.Imp.PageConverter;
import com.metabubble.BWC.dto.MerchantDto;
import com.metabubble.BWC.entity.Merchant;
import com.metabubble.BWC.service.ConfigService;
import com.metabubble.BWC.service.LogsService;
import com.metabubble.BWC.service.MerchantService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

@RestController
@RequestMapping("/merchant")
@Slf4j
public class MerchantController {

    @Autowired
    MerchantService merchantService;
    @Autowired
    LogsService logsService;
    @Autowired
    ConfigService configService;

//    public static final String KEY_1 = "XEABZ-GFERQ-GVY5M-GZCOR-EJGOT-OWBOP";

    /**
     * 查询商家信息
     * @param limit
     * @param offset
     * @param name
     * @param tel
     * @Author 看客
     * @return
     */
    @GetMapping
    public R<Page> getMerchantByPage(Integer limit,Integer offset,String name,String tel) {

        Page<Merchant> merchantPage = new Page<>(offset, limit);
        LambdaQueryWrapper<Merchant> mLqw = new LambdaQueryWrapper<>();
        //添加过滤条件
        mLqw.like(StringUtils.isNotEmpty(name), Merchant::getName, name);

        mLqw.like(StringUtils.isNotEmpty(tel),Merchant::getTel,tel);

        //添加排序条件
        mLqw.orderByDesc(Merchant::getCreateTime);

        merchantService.page(merchantPage,mLqw);
        List<Merchant> records = merchantPage.getRecords();
        List<MerchantDto> merchants = new ArrayList<>();
        if (records != null){
            for(Merchant record:records){
                if (record !=null) {
                    MerchantDto merchantDto = MerchantConverter.INSTANCES.MerchantToMerchantDto(record);
                    merchants.add(merchantDto);
                }
            }
        }
        Page page = PageConverter.INSTANCES.PageToPage(merchantPage);
        page.setRecords(merchants);
        return R.success(page);
    }

    /**
     * 根据id获取商家信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<MerchantDto> getMerchantById(@PathVariable("id") Long id){
        Merchant merchant = merchantService.getById(id);
        MerchantDto merchantDto = MerchantConverter.INSTANCES.MerchantToMerchantDto(merchant);
        return R.success(merchantDto);
    }

    /**
     * 新增商家
     * @param merchant
     * @Author 看客
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody Merchant merchant) {
        //获取经纬度
        BigDecimal lng = merchant.getLng();
        BigDecimal lat = merchant.getLat();
        if (lng == null  || lat == null){
            lng = getGeocoderLatitude(merchant.getAddress()).get("lng");
            lat = getGeocoderLatitude(merchant.getAddress()).get("lat");
        }

        merchant.setLng(lng);
        merchant.setLat(lat);
        //保存
        boolean flag = merchantService.save(merchant);
        if (flag){
            logsService.saveLog("新增商家","新增了\""+merchant.getName()+"\"商家");
            return R.success("新增成功");
        }

       return R.error("新增失败");
    }

    /**
     * 修改商家信息
     * @param merchant
     * @Author 看客
     * @return
     */
    @PutMapping
    public R<String> update(@RequestBody Merchant merchant) {
        //获取修改前的商家
        Merchant merchantOld = merchantService.getById(merchant.getId());
        //获取修改前的商家名字
        String merchantName = merchantOld.getName();
        //给手机号之间增加一个逗号分隔开
        if (merchant.getBlacklist() != null){
            //获取之前的黑名单手机号
            String blacklist = merchantOld.getBlacklist();
            String newBlacklist = blacklist + "," + merchant.getBlacklist();
            merchant.setBlacklist(newBlacklist);
        }
        boolean flag = merchantService.updateById(merchant);
        if (flag){
            logsService.saveLog("修改商家","修改了\""+merchantName+"\"商家的基本信息");
            return R.success("修改成功");
        }
        return R.error("修改失败");

    }

    /**
     * 删除商家
     * @param id
     * @Author 看客
     * @return
     */
    @DeleteMapping("/{id}")
    public R<String> delete(@PathVariable("id") Long id){
        String name = merchantService.getById(id).getName();
        boolean flag = merchantService.removeById(id);
        if(flag){
            logsService.saveLog("删除商家","删除了\""+name+"\"商家");
            return R.success("删除成功");
        }
        return R.error("删除失败");
    }

    /**
     * 返回输入地址的经纬度坐标
     * key lng(经度),lat(纬度)
     * @param address
     * @Author 看客
     * @return
     */
    @GetMapping("/getPosition")
    public Map<String, BigDecimal> getGeocoderLatitude(String address) {
        String key = configService.getById(10).getContent();
        BufferedReader in = null;
        try {
            //将地址转换成utf-8的16进制
            address = URLEncoder.encode(address, "UTF-8");
            URL tirc = new URL("https://apis.map.qq.com/ws/geocoder/v1/?address=" + address + "&output=json&key=" + key);

            in = new BufferedReader(new InputStreamReader(tirc.openStream(), StandardCharsets.UTF_8));
            String res;
            StringBuilder sb = new StringBuilder("");
            while ((res = in.readLine()) != null) {
                sb.append(res.trim());
            }
            String str = sb.toString();
            Map<String, BigDecimal> map = null;
            if (StringUtils.isNotEmpty(str)) {
                int lngStart = str.indexOf("lng\":");
                int lngEnd = str.indexOf(",\"lat");
                int latEnd = str.indexOf("}", lngEnd + 8);
                if (lngStart > 0 && lngEnd > 0 && latEnd > 0) {
                    String lngStr = str.substring(lngStart + 5, lngEnd);
                    String latStr = str.substring(lngEnd + 7, latEnd);
                    BigDecimal lng = BigDecimal.valueOf(Double.parseDouble(lngStr));
                    BigDecimal lat = BigDecimal.valueOf(Double.parseDouble(latStr));
                    map = new HashMap<>();
                    map.put("lng", lng);
                    map.put("lat", lat);
                    return map;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 输入经纬度获取地址
     * @param lng
     * @param lat
     * @return
     */
    @GetMapping("/getAddress")
    public String getAdd(String lng, String lat ){
        String key = configService.getById(10).getContent();
        String urlString = "https://apis.map.qq.com/ws/geocoder/v1/?location="+lat+","+lng+"&output=json&key="+ key;
        String res = "";
        try {
            URL url = new URL(urlString);
            java.net.HttpURLConnection conn = (java.net.HttpURLConnection)url.openConnection();
            java.io.BufferedReader in = new java.io.BufferedReader(new java.io.InputStreamReader(conn.getInputStream(),"UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                res += line+"\n";
            }
            in.close();
        } catch (Exception e) {
            System.out.println("error in wapaction,and e is " + e.getMessage());
        }
        System.out.println(res);
        return res;
    }

    @GetMapping("/getLocation")
    public String getTXCityCodeByIp(HttpServletRequest request) {
        String clientIp = getClientIp(request);
        String key = configService.getById(10).getContent();
        String urlString = "https://apis.map.qq.com/ws/location/v1/ip?ip="+clientIp+"&key="+ key;
        String res = "";
        try {
            URL url = new URL(urlString);
            java.net.HttpURLConnection conn = (java.net.HttpURLConnection)url.openConnection();
            java.io.BufferedReader in = new java.io.BufferedReader(new java.io.InputStreamReader(conn.getInputStream(),"UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                res += line+"\n";
            }
            in.close();
        } catch (Exception e) {
            System.out.println("error in wapaction,and e is " + e.getMessage());
        }
        System.out.println(res);
        return res;
    }

    public String getClientIp(HttpServletRequest request) {
        String xff = request.getHeader("X-Forwarded-For");
        if (xff == null) {
            return request.getRemoteAddr();
        } else {
            return xff.contains(",") ? xff.split(",")[0] : xff;
        }
    }
}

