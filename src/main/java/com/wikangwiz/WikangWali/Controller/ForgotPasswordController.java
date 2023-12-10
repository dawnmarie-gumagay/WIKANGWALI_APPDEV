package com.wikangwiz.WikangWali.Controller;

import com.wikangwiz.WikangWali.Entity.StudentEntity;
import com.wikangwiz.WikangWali.Methods.ForgotPasswordRequest;
import com.wikangwiz.WikangWali.Methods.ResetPasswordRequest;
import com.wikangwiz.WikangWali.Service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Random;

@RestController
@RequestMapping("/student/forgotPassword")
public class ForgotPasswordController {

    @Autowired
    private StudentService studentService;

    // Endpoint for initiating the forgot password process
    @PostMapping("/generateCode")
    public ResponseEntity<String> generateResetCode(@RequestBody ForgotPasswordRequest forgotPasswordRequest) {
        String username = forgotPasswordRequest.getUsername();

        try {
            StudentEntity student = studentService.findStudentByUsername(username);
            if (student == null || student.getIsDeleted()) {
                throw new IllegalArgumentException("Student not found with username: " + username);
            }
            // Generate a random 4-digit code
            int resetCode = new Random().nextInt(9000) + 1000;

            // TODO: Send the reset code to the user (you can use email, SMS, etc.)
            System.out.print("The reset code is " + resetCode);

            // Store the reset code in the database
            student.setResetCode(resetCode);
            studentService.updateStudentObject(student);
            System.out.print(" The reset code for " + username+ " is " + resetCode);

            return ResponseEntity.ok("Reset code generated successfully. Check console for the code.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Endpoint for resetting the password after confirming the reset code
    @PostMapping("/resetPassword")
    public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordRequest resetPasswordRequest) {
        String username = resetPasswordRequest.getUsername();
        int resetCode = resetPasswordRequest.getResetCode();
        String newPassword = resetPasswordRequest.getNewPassword();
        String confirmPassword = resetPasswordRequest.getConfirmPassword();

        try {
            StudentEntity student = studentService.findStudentByUsername(username);

            if (student == null || student.getIsDeleted()) {
                throw new IllegalArgumentException("Student not found with username: " + username);
            }

            if (student.getResetCode() != resetCode) {
                throw new IllegalArgumentException("Invalid reset code. Please check your code again.");
            }

            // Check if newPassword is different from the old password
            if (newPassword.equals(student.getPassword())) {
                throw new IllegalArgumentException("New password must be different from the old password.");
            }

            // Check if newPassword matches confirmPassword
            if (!newPassword.equals(confirmPassword)) {
                throw new IllegalArgumentException("New password and confirm password do not match.");
            }

            // Update the password and reset code
            student.setPassword(newPassword);
            student.setResetCode(0); // Clear the reset code after successful reset

            studentService.updateStudentObject(student);

            return ResponseEntity.ok("Password reset successfully.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
