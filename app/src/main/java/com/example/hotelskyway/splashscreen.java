package com.example.hotelskyway;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.material.snackbar.Snackbar;

import java.text.DateFormat;
import java.util.Date;

import frlgrd.animatededittext.AnimatedEditText;

public class splashscreen extends AppCompatActivity {
    ConstraintLayout SplashLayout,AuthLayout,Main;
    LinearLayout LoginLayout,SignupLayout,ChangePassword,ForgetPassword;
    AnimatedEditText AuthEmail,AuthPassword,LastName,FirstName,Email,Password,ConfirmPassword,AccessCode,Mobile,ForgetEmail,ForgetMobile,ChangeNewPass,ChangeConfirmPass;
    Button Submit;
    TextView Suggestion,RegLog,Tv2;
    ImageView Noti;
    //public static MyAppDatabase myAppDatabase;
    Snackbar mSnackbar;

    Database database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_splashscreen );

        database= new Database ( this );


        Main=(ConstraintLayout) findViewById ( R.id.main );
        SplashLayout =(ConstraintLayout) findViewById ( R.id.splashLayout);
        AuthLayout =(ConstraintLayout) findViewById ( R.id.authLayout);
        LoginLayout=(LinearLayout) findViewById ( R.id.loginLayout );
        SignupLayout=(LinearLayout) findViewById ( R.id.signupLayout );
        ForgetPassword=(LinearLayout) findViewById ( R.id.forgetPassword );
        ChangePassword=(LinearLayout) findViewById ( R.id.changePassword );
        AuthEmail=(AnimatedEditText) findViewById ( R.id.authEmail );
        AuthPassword=(AnimatedEditText) findViewById ( R.id.authPassword );
        FirstName=(AnimatedEditText) findViewById ( R.id.firstName );
        LastName=(AnimatedEditText) findViewById ( R.id.lastName ) ;
        Email=(AnimatedEditText) findViewById ( R.id. email) ;
        Password=(AnimatedEditText) findViewById ( R.id.Password) ;
        ConfirmPassword=(AnimatedEditText) findViewById ( R.id. confirmPassword) ;
        AccessCode=(AnimatedEditText) findViewById ( R.id.accessCode );
        Mobile =(AnimatedEditText) findViewById ( R.id.Mobile );
        ForgetEmail= (AnimatedEditText) findViewById ( R.id.forgetEmail ) ;
        ForgetMobile=(AnimatedEditText) findViewById ( R.id. forgetMobile) ;
        ChangeNewPass=(AnimatedEditText) findViewById ( R.id.changenewPass ) ;
        ChangeConfirmPass=(AnimatedEditText) findViewById ( R.id.changeConfirmpass ) ;
        Submit=(Button) findViewById ( R.id.submit );
        Suggestion=(TextView) findViewById ( R.id.suggestion );
        Tv2=(TextView) findViewById ( R.id.textView2 );
        RegLog=(TextView) findViewById ( R.id.reglog );
        Noti=(ImageView) findViewById ( R.id.info );
        new Handler ().postDelayed( new Runnable() {
            @Override
            public void run() {
                long ActiveUser= database.LoginStatus ( "ACTIVE" );
                if(ActiveUser==1){
                    //Toast.makeText ( splashscreen.this,a , Toast.LENGTH_SHORT ).show ();
                    Intent i =new Intent ( splashscreen.this,MainActivity.class );
                    startActivity ( i );
                    finish ();

                }
                else {
                    SplashLayout.setVisibility ( View.GONE );
                    AuthLayout.setVisibility ( View.VISIBLE );
                }


            }
        },2800);
        Submit.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                if (Submit.getText ().toString ().trim ().equals ( "LOGIN" ))
                {
                    String authemail = AuthEmail.getEditText ().getText ().toString ().trim ();
                    String authpassword= AuthPassword.getEditText ().getText ().toString ().trim ();
                    if(!authemail.isEmpty () && !authpassword.isEmpty ())
                    {

                            Boolean res = database.checkUserForLogin ( authemail,authpassword,"ACTIVE");
                            if (res==true)
                            {
                                mSnackbar = Snackbar.make(AuthLayout,"successfully Logged In", Snackbar.LENGTH_SHORT);
                                mSnackbar.show();
                                Intent i =new Intent ( splashscreen.this,MainActivity.class );
                                startActivity ( i );
                                finish ();
                            }
                            else
                            {
                                mSnackbar = Snackbar.make(AuthLayout,"Either user doesn't exist or password miss match", Snackbar.LENGTH_LONG);
                                mSnackbar.show();
                            }
                    }
                    else{
                        if (authemail.isEmpty ()){
                            AuthEmail.expand ();
                            AuthEmail.getEditText ().setError ( "Enter Email" );
                        }
                        if (authpassword.isEmpty ()){
                            AuthPassword.expand ();
                            AuthPassword.getEditText ().setError ( "Enter Your Password" );
                        }
                    }
                }
                else if (Submit.getText ().toString ().trim ().equals ( "REGISTER" ))
                {
                    String accesscode = AccessCode.getEditText ().getText ().toString ().trim ();
                    String firstname = FirstName.getEditText ().getText ().toString ().trim ();
                    String lastname = LastName.getEditText ().getText ().toString ().trim ();
                    String email = Email.getEditText ().getText ().toString ().trim ();
                    String mobile= Mobile.getEditText ().getText ().toString ().trim ();
                    String pass = Password.getEditText ().getText ().toString ().trim ();
                    String confirmpass = ConfirmPassword.getEditText ().getText ().toString ().trim ();
                    if(!firstname.isEmpty () && !lastname.isEmpty () && !email.isEmpty () && !pass.isEmpty()  && !confirmpass.isEmpty () && !accesscode.isEmpty () && !mobile.isEmpty ()){
                        if (pass.equals ( confirmpass ) && pass.length ()>=6 && accesscode.equals ( "1234" ) && mobile.length ()==10)
                        {
                            String date = DateFormat.getDateInstance().format(new Date ());
                            long val = database.addUser ( email,pass,firstname,lastname,mobile,"MANAGER","NOTActive","MANAGER",date );
                            if (val >0)
                            {

                                FirstName.getEditText ().setText ( "" );
                                LastName.getEditText ().setText ( "" );
                                Email.getEditText ().setText ( "" );
                                Password.getEditText ().setText ( "" );
                                ConfirmPassword.getEditText ().setText ( "" );
                                AccessCode.getEditText ().setText ( "" );
                                Mobile.getEditText ().setText ( "" );
                                mSnackbar = Snackbar.make(Main,"Registration Successful... LOGIN NOW", Snackbar.LENGTH_SHORT);
                                mSnackbar.setDuration ( 3000 );
                                mSnackbar.show ();
                            }
                            else
                            {
                                mSnackbar = Snackbar.make(AuthLayout,"Registration Failed", Snackbar.LENGTH_LONG);
                                mSnackbar.show();
                            }


                        }else
                        {
                            if (pass.length ()<6)
                            {
                                Password.getEditText ().setError ( "password must be greater 6 character" );
                            }else if (!accesscode.equals ( "1234" ))
                            {
                                Toast.makeText ( splashscreen.this, "Access Code is Wrong", Toast.LENGTH_SHORT ).show ();
                            }
                            else{
                            Toast.makeText ( splashscreen.this, "Password is not same", Toast.LENGTH_LONG ).show ();}
                        }

                    }
                    else{
                        if (firstname.isEmpty ()){
                            FirstName.expand ();
                            FirstName.getEditText ().setError ( "Enter First Name" );
                        }
                        if (lastname.isEmpty ()){
                            LastName.expand ();
                            LastName.getEditText ().setError ( "Enter Last Name" );
                        }
                        if(email.isEmpty ()){
                            Email.expand ();
                            Email.getEditText ().setError ( "Enter Email" );
                        }
                        if (pass.isEmpty ()){
                            Password.expand ();
                            Password.getEditText ().setError ( "Enter Password" );
                        }
                        if(confirmpass.isEmpty ()){
                            ConfirmPassword.expand ();
                            ConfirmPassword.getEditText ().setError ( "Confirm Your Password" );
                        }
                        if(accesscode.isEmpty ()){
                            AccessCode.expand ();
                            AccessCode.getEditText ().setError ( "Enter Access code" );
                        }
                    }

                }
                else if (Submit.getText ().toString ().trim ().equals ( "SUBMIT" ))
                {

                    String email = ForgetEmail.getEditText ().getText ().toString ().trim ();
                    String mobile = ForgetMobile.getEditText ().getText ().toString ().trim ();
                    if(!email.isEmpty () && !mobile.isEmpty ()){
                        if (  mobile.length ()==10)
                        {
                            boolean req = database.checkUserForPasswordChange ( email,mobile );
                            if (req==true){
                                ForgetPassword.setVisibility ( View.GONE );
                                Submit.setText ( "CHANGE_PASSWORD" );
                                ChangePassword.setVisibility ( View.VISIBLE );
                            }
                            else{
                                mSnackbar = Snackbar.make(AuthLayout,"Data Doesn't match", Snackbar.LENGTH_LONG);
                                mSnackbar.show();
                            }
                        }else
                        {
                            ForgetMobile.expand ();
                            ForgetMobile.getEditText ().setError ( "Enter valid Number" );
                        }
                    }
                    else{

                        if(email.isEmpty ()){
                            ForgetEmail.expand ();
                            ForgetEmail.getEditText ().setError ( "Enter Email" );
                        }
                        if (mobile.isEmpty ()){
                            ForgetMobile.expand ();
                            ForgetMobile.getEditText ().setError ( "Enter Password" );
                        }
                    }


                }
                else if (Submit.getText ().toString ().trim ().equals ( "CHANGE_PASSWORD" ))
                {
                    String changepass= ChangeNewPass.getEditText ().getText ().toString ().trim ();
                    String changeConfirmpass =ChangeConfirmPass.getEditText ().getText ().toString ().trim ();
                    if (changepass.equals ( changeConfirmpass ) && changepass.length ()>=6){
                        long val =database.updatepassword ( ForgetEmail.getEditText ().getText ().toString ().trim (),changepass );
                        if (val>0)
                        {
                            ChangePassword.setVisibility (View.GONE  );
                            LoginLayout.setVisibility ( View.VISIBLE );
                            Suggestion.setVisibility ( View.VISIBLE );
                            Submit.setText ( "LOGIN" );
                            mSnackbar = Snackbar.make(AuthLayout,"Password Updated Successfully", Snackbar.LENGTH_LONG);
                            mSnackbar.show();

                        }
                        else {
                            Toast.makeText ( splashscreen.this, "Update Failed ", Toast.LENGTH_SHORT ).show ();
                        }
                    }
                    else {
                        if (!changepass.equals ( changeConfirmpass )){
                            Toast.makeText ( splashscreen.this, "Password Doesn't match", Toast.LENGTH_SHORT ).show ();
                        }
                        if (changepass.length ()<6){
                            ChangeNewPass.expand ();
                            ChangeNewPass.getEditText ().setError ( "Password should be greater than 5 Character" );
                        }
                    }
                }

            }
        } );

        RegLog.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                if (RegLog.getText ().equals ( "REGISTER" )){
                    LoginLayout.setVisibility ( View.GONE );
                    Suggestion.setVisibility ( View.GONE );
                    Tv2.setText ( "Already Have Account?" );
                    RegLog.setText ( "LOGIN" );
                    Submit.setText ( "REGISTER" );
                    SignupLayout.setVisibility ( View.VISIBLE );
                    AuthPassword.getEditText ().setText ( "" );
                    AuthEmail.getEditText ().setText ( "" );
                }
                else if (RegLog.getText ().equals ( "LOGIN" )){
                    SignupLayout.setVisibility ( View.GONE );
                    Suggestion.setVisibility ( View.VISIBLE );
                    Tv2.setText ( "Not Registered?" );
                    RegLog.setText ( "REGISTER" );
                    Submit.setText ( "LOGIN" );
                    ForgetPassword.setVisibility ( View.GONE );
                    ChangePassword.setVisibility ( View.GONE );
                    LoginLayout.setVisibility ( View.VISIBLE );
                }
            }
        } );
        Suggestion.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                LoginLayout.setVisibility ( View.GONE );
                Suggestion.setVisibility ( View.GONE );
                ForgetPassword.setVisibility ( View.VISIBLE );
                Tv2.setText ( "Recalled Your Password" );
                RegLog.setText ( "LOGIN" );
                Submit.setText ( "SUBMIT" );


            }
        } );
        Noti.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                mSnackbar = Snackbar.make(Main,"Ask Your Owner To Enter Access Code( Use Free Code -->> 1234 ) ", Snackbar.LENGTH_LONG);
                mSnackbar.show ();
            }
        } );
        mSnackbar = Snackbar.make(AuthLayout,"Press again to exit", Snackbar.LENGTH_SHORT);
    }
    int i=0;
    @Override
    public void onBackPressed() {

        if (mSnackbar.isShown()) {
            super.onBackPressed();
        } else {
            mSnackbar.show();
        }

    }
}
