@Service
@Slf4j
public class FraudAnalysisAIService {

    @Autowired
    private OpenAIClient openAIClient;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * 使用Function Calling分析测试结果
     */
    public FraudAnalysisResult analyzeTestResultWithFunctionCalling(TestResultVO testResult) {
        try {
            // 1. 构建分析提示词
            String analysisPrompt = buildAnalysisPrompt(testResult);

            // 2. 创建Function定义
            FunctionDefinition function = FunctionDefinition.createFraudAnalysisFunction();

            // 3. 构建请求
            ChatCompletionRequest request = ChatCompletionRequest.builder()
                .model(myModel)
                .messages(Arrays.asList(
                    ChatMessage.builder()
                        .role("system")
                        .content("你是一位专业的防诈骗安全专家，请根据用户的测试结果进行深度分析并提供专业建议。")
                        .build(),
                    ChatMessage.builder()
                        .role("user")
                        .content(analysisPrompt)
                        .build()
                ))
                .functions(Collections.singletonList(function))
                .functionCall(FunctionCall.builder().name("analyze_fraud_test_result").build())
                .temperature(0.1) // 降低随机性
                .build();

            // 4. 调用API
            ChatCompletionResponse response = openAIClient.createChatCompletion(request);

            // 5. 解析Function Call结果
            ChatMessage responseMessage = response.getChoices().get(0).getMessage();
            if (responseMessage.getFunctionCall() != null) {
                String functionArgs = responseMessage.getFunctionCall().getArguments();
                return objectMapper.readValue(functionArgs, FraudAnalysisResult.class);
            } else {
                throw new RuntimeException("AI未返回Function Call结果");
            }

        } catch (Exception e) {
            log.error("Function Calling分析失败", e);
            // 降级到传统方式
            return analyzeWithFallback(testResult);
        }
    }

    /**
     * 构建分析提示词
     */
    private String buildAnalysisPrompt(TestResultVO testResult) {
        StringBuilder prompt = new StringBuilder();

        prompt.append("## 用户红色文化测试结果分析\n\n");
        prompt.append("### 基本信息\n");
        prompt.append("- 测试名称：").append(testResult.getTestTitle()).append("\n");
        prompt.append("- 总分：").append(testResult.getScore()).append("分\n");
        prompt.append("- 答题情况：答对").append(testResult.getCorrectCount())
              .append("题，答错").append(testResult.getWrongCount())
              .append("题，共").append(testResult.getTotalCount()).append("题\n");
        prompt.append("- 正确率：").append(testResult.getAccuracy()).append("%\n");
        prompt.append("- 用时：").append(formatTime(testResult.getTimeUsed())).append("\n\n");

        // 错误分析
        Map<String, List<TestDetailVO>> errorsByCategory = testResult.getDetails()
            .stream()
            .filter(detail -> !detail.getIsCorrect())
            .collect(Collectors.groupingBy(detail -> detail.getQuestion().getCategory()));

        if (!errorsByCategory.isEmpty()) {
            prompt.append("### 错误题目分析\n");
            errorsByCategory.forEach((category, errors) -> {
                prompt.append("**").append(getCategoryName(category)).append("类错误：**\n");
                errors.forEach(error -> {
                    prompt.append("- ").append(error.getQuestion().getTitle()).append("\n");
                    prompt.append("  用户答案：").append(error.getUserAnswer()).append("\n");
                    prompt.append("  正确答案：").append(error.getCorrectAnswer()).append("\n");
                });
                prompt.append("\n");
            });
        }

        // 正确题目分析
        Map<String, Long> correctByCategory = testResult.getDetails()
            .stream()
            .filter(TestDetailVO::getIsCorrect)
            .collect(Collectors.groupingBy(
                detail -> detail.getQuestion().getCategory(),
                Collectors.counting()
            ));

        if (!correctByCategory.isEmpty()) {
            prompt.append("### 正确题目分析\n");
            correctByCategory.forEach((category, count) -> {
                prompt.append("- ").append(getCategoryName(category))
                      .append("：答对").append(count).append("题\n");
            });
            prompt.append("\n");
        }

        prompt.append("请基于以上数据，调用analyze_fraud_test_result函数进行专业分析。");

        return prompt.toString();
    }

    /**
     * 降级分析方法
     */
    private FraudAnalysisResult analyzeWithFallback(TestResultVO testResult) {
        // 实现传统的规则基分析逻辑
        FraudAnalysisResult result = new FraudAnalysisResult();

        double accuracy = testResult.getAccuracy();

        // 基于准确率设置风险等级
        if (accuracy >= 90) {
            result.setRiskLevel(1);
            result.setOverallAssessment("您的防诈骗意识非常强，能够准确识别各类诈骗手段。");
        } else if (accuracy >= 70) {
            result.setRiskLevel(2);
            result.setOverallAssessment("您具备良好的防诈骗基础知识，但在某些方面还需要加强。");
        } else if (accuracy >= 50) {
            result.setRiskLevel(3);
            result.setOverallAssessment("您的防诈骗意识有待提升，建议系统学习相关知识。");
        } else {
            result.setRiskLevel(4);
            result.setOverallAssessment("您的防诈骗意识较弱，需要重点加强学习和防范。");
        }

        // 分析优势和薄弱环节
        analyzeStrengthsAndWeaknesses(testResult, result);

        // 生成建议
        generateRecommendations(testResult, result);

        return result;
    }

    private void analyzeStrengthsAndWeaknesses(TestResultVO testResult, FraudAnalysisResult result) {
        Map<String, Double> categoryAccuracy = new HashMap<>();
        Map<String, Integer> categoryTotal = new HashMap<>();
        Map<String, Integer> categoryCorrect = new HashMap<>();

        // 统计各类别的准确率
        testResult.getDetails().forEach(detail -> {
            String category = detail.getQuestion().getCategory();
            categoryTotal.merge(category, 1, Integer::sum);
            if (detail.getIsCorrect()) {
                categoryCorrect.merge(category, 1, Integer::sum);
            }
        });

        categoryTotal.forEach((category, total) -> {
            int correct = categoryCorrect.getOrDefault(category, 0);
            double accuracy = (double) correct / total * 100;
            categoryAccuracy.put(category, accuracy);
        });

        // 识别优势领域（准确率>=80%）
        List<String> strengths = categoryAccuracy.entrySet().stream()
            .filter(entry -> entry.getValue() >= 80.0)
            .map(entry -> getCategoryName(entry.getKey()))
            .collect(Collectors.toList());
        result.setStrengths(strengths);

        // 识别薄弱环节（准确率<60%）
        List<String> weaknesses = categoryAccuracy.entrySet().stream()
            .filter(entry -> entry.getValue() < 60.0)
            .map(entry -> getCategoryName(entry.getKey()))
            .collect(Collectors.toList());
        result.setWeaknesses(weaknesses);
    }

    private void generateRecommendations(TestResultVO testResult, FraudAnalysisResult result) {
        List<Recommendation> recommendations = new ArrayList<>();

        // 基于薄弱环节生成建议
        result.getWeaknesses().forEach(weakness -> {
            Recommendation rec = new Recommendation();
            rec.setCategory(weakness + "防范");
            rec.setTitle("加强" + weakness + "识别能力");
            rec.setDescription("重点学习" + weakness + "的常见手段和防范方法");
            rec.setPriority(1);
            rec.setActions(Arrays.asList(
                "学习" + weakness + "的典型案例",
                "掌握" + weakness + "的识别要点",
                "练习" + weakness + "的应对方法"
            ));
            recommendations.add(rec);
        });

        result.setRecommendations(recommendations);
    }

    private String getCategoryName(String category) {
        Map<String, String> categoryNames = Map.of(
            "telecom", "电信诈骗",
            "financial", "金融诈骗",
            "network", "网络诈骗",
            "shopping", "购物诈骗",
            "romance", "情感诈骗",
            "email", "邮件诈骗",
            "job", "招聘诈骗",
            "property", "房产诈骗"
        );
        return categoryNames.getOrDefault(category, category);
    }

    private String formatTime(Long timeUsed) {
        if (timeUsed == null) return "未知";
        long minutes = timeUsed / 60;
        long seconds = timeUsed % 60;
        return String.format("%d分%d秒", minutes, seconds);
    }
}