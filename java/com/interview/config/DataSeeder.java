package com.interview.config;

import com.interview.entity.*;
import com.interview.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataSeeder.class);

    private final UserRepository userRepository;
    private final CodingQuestionRepository codingQuestionRepository;
    private final TestCaseRepository testCaseRepository;
    private final InterviewQuestionRepository interviewQuestionRepository;
    private final PasswordEncoder passwordEncoder;

    public DataSeeder(UserRepository userRepository,
                      CodingQuestionRepository codingQuestionRepository,
                      TestCaseRepository testCaseRepository,
                      InterviewQuestionRepository interviewQuestionRepository,
                      PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.codingQuestionRepository = codingQuestionRepository;
        this.testCaseRepository = testCaseRepository;
        this.interviewQuestionRepository = interviewQuestionRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (userRepository.count() > 0) {
            log.info("Data already seeded, skipping...");
            return;
        }
        seedUsers();
        seedCodingQuestions();
        seedInterviewQuestions();
        log.info("Demo data seeded successfully!");
    }

    private void seedUsers() {
        User recruiter = User.builder()
                .fullName("Sarah Johnson")
                .email("recruiter@demo.com")
                .password(passwordEncoder.encode("password123"))
                .role(User.Role.RECRUITER)
                .phone("+1-555-0101")
                .enabled(true).build();
        userRepository.save(recruiter);

        User candidate1 = User.builder()
                .fullName("Alex Chen")
                .email("candidate@demo.com")
                .password(passwordEncoder.encode("password123"))
                .role(User.Role.CANDIDATE)
                .phone("+1-555-0201")
                .enabled(true).build();
        userRepository.save(candidate1);

        User candidate2 = User.builder()
                .fullName("Priya Sharma")
                .email("priya@demo.com")
                .password(passwordEncoder.encode("password123"))
                .role(User.Role.CANDIDATE)
                .phone("+1-555-0202")
                .enabled(true).build();
        userRepository.save(candidate2);
        log.info("Users seeded");
    }

    private void seedCodingQuestions() {
        CodingQuestion q1 = CodingQuestion.builder()
                .title("Two Sum")
                .description("Given an array of integers nums and an integer target, return indices of the two numbers such that they add up to target.\n\nYou may assume that each input would have exactly one solution, and you may not use the same element twice.\n\nReturn the answer in any order.")
                .difficulty(CodingQuestion.Difficulty.EASY)
                .sampleInput("nums = [2,7,11,15], target = 9")
                .sampleOutput("[0,1]")
                .constraints("2 <= nums.length <= 10^4\n-10^9 <= nums[i] <= 10^9\n-10^9 <= target <= 10^9")
                .totalMarks(100)
                .timeLimitMinutes(30)
                .active(true).build();
        codingQuestionRepository.save(q1);

        testCaseRepository.save(TestCase.builder().question(q1).input("[2,7,11,15]\n9").expectedOutput("[0,1]").hidden(false).marks(25).build());
        testCaseRepository.save(TestCase.builder().question(q1).input("[3,2,4]\n6").expectedOutput("[1,2]").hidden(false).marks(25).build());
        testCaseRepository.save(TestCase.builder().question(q1).input("[3,3]\n6").expectedOutput("[0,1]").hidden(true).marks(25).build());
        testCaseRepository.save(TestCase.builder().question(q1).input("[1,2,3,4,5]\n9").expectedOutput("[3,4]").hidden(true).marks(25).build());

        CodingQuestion q2 = CodingQuestion.builder()
                .title("Reverse Linked List")
                .description("Given the head of a singly linked list, reverse the list, and return the reversed list.\n\nImplement a function that takes the head node and returns the head of the reversed list.")
                .difficulty(CodingQuestion.Difficulty.EASY)
                .sampleInput("head = [1,2,3,4,5]")
                .sampleOutput("[5,4,3,2,1]")
                .constraints("The number of nodes in the list is in the range [0, 5000]\n-5000 <= Node.val <= 5000")
                .totalMarks(100)
                .timeLimitMinutes(30)
                .active(true).build();
        codingQuestionRepository.save(q2);

        testCaseRepository.save(TestCase.builder().question(q2).input("[1,2,3,4,5]").expectedOutput("[5,4,3,2,1]").hidden(false).marks(50).build());
        testCaseRepository.save(TestCase.builder().question(q2).input("[1,2]").expectedOutput("[2,1]").hidden(true).marks(50).build());

        CodingQuestion q3 = CodingQuestion.builder()
                .title("Valid Parentheses")
                .description("Given a string s containing just the characters '(', ')', '{', '}', '[' and ']', determine if the input string is valid.\n\nAn input string is valid if:\n1. Open brackets must be closed by the same type of brackets.\n2. Open brackets must be closed in the correct order.\n3. Every close bracket has a corresponding open bracket of the same type.")
                .difficulty(CodingQuestion.Difficulty.MEDIUM)
                .sampleInput("s = \"()[]{}\"")
                .sampleOutput("true")
                .constraints("1 <= s.length <= 10^4\ns consists of parentheses only '()[]{}'")
                .totalMarks(150)
                .timeLimitMinutes(45)
                .active(true).build();
        codingQuestionRepository.save(q3);

        testCaseRepository.save(TestCase.builder().question(q3).input("()").expectedOutput("true").hidden(false).marks(30).build());
        testCaseRepository.save(TestCase.builder().question(q3).input("()[]{}").expectedOutput("true").hidden(false).marks(30).build());
        testCaseRepository.save(TestCase.builder().question(q3).input("(]").expectedOutput("false").hidden(false).marks(30).build());
        testCaseRepository.save(TestCase.builder().question(q3).input("{[]}").expectedOutput("true").hidden(true).marks(30).build());
        testCaseRepository.save(TestCase.builder().question(q3).input("([)]").expectedOutput("false").hidden(true).marks(30).build());

        CodingQuestion q4 = CodingQuestion.builder()
                .title("LRU Cache")
                .description("Design a data structure that follows the constraints of a Least Recently Used (LRU) cache.\n\nImplement the LRUCache class:\n- LRUCache(int capacity): Initialize the LRU cache with positive size capacity.\n- int get(int key): Return the value of the key if it exists, otherwise return -1.\n- void put(int key, int value): Update the value of the key if it exists. Otherwise, add the key-value pair to the cache. If the number of keys exceeds the capacity, evict the least recently used key.")
                .difficulty(CodingQuestion.Difficulty.HARD)
                .sampleInput("capacity = 2\nput(1,1), put(2,2), get(1), put(3,3), get(2)")
                .sampleOutput("[null,null,1,null,-1]")
                .constraints("1 <= capacity <= 3000\n0 <= key <= 10^4\n0 <= value <= 10^5")
                .totalMarks(200)
                .timeLimitMinutes(60)
                .active(true).build();
        codingQuestionRepository.save(q4);

        testCaseRepository.save(TestCase.builder().question(q4).input("2\nput(1,1)\nput(2,2)\nget(1)").expectedOutput("1").hidden(false).marks(100).build());
        testCaseRepository.save(TestCase.builder().question(q4).input("2\nput(1,1)\nput(2,2)\nget(1)\nput(3,3)\nget(2)").expectedOutput("-1").hidden(true).marks(100).build());

        log.info("Coding questions seeded");
    }

    private void seedInterviewQuestions() {
        String[][] questions = {
            {"Tell me about yourself and your background in software development.", "Give a brief professional introduction covering your experience, skills, and what motivates you.", "0"},
            {"Describe a challenging technical problem you solved recently.", "Walk us through a specific technical challenge, your approach, and the outcome.", "1"},
            {"How do you approach learning new technologies?", "Explain your learning strategy and give examples of technologies you've recently picked up.", "2"},
            {"Explain the concept of microservices and when you would use them.", "Describe microservices architecture, its benefits, challenges, and appropriate use cases.", "3"},
            {"How do you ensure code quality in your projects?", "Discuss testing strategies, code reviews, and best practices you follow.", "4"}
        };

        for (String[] q : questions) {
            InterviewQuestion question = InterviewQuestion.builder()
                    .questionText(q[0])
                    .description(q[1])
                    .maxDurationSeconds(180)
                    .active(true)
                    .orderIndex(Integer.parseInt(q[2]))
                    .build();
            interviewQuestionRepository.save(question);
        }
        log.info("Interview questions seeded");
    }
}
