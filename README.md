# CIS-4515-Lab-02

CIS 4515

Worksheet 2

Instructions:
Create and application that implements a Content Provider that can generate and return public and
private keys for use in an asymmetric encryption system. Thereafter create a client (within the same
application) that utilizes the Content Provider to retrieve a set of keys, and can encrypt and decrypt text
that is entered by the user.
1. Familiarize yourself with the process of Public Key Cryptography
(https://en.wikipedia.org/wiki/Public-key_cryptography)
2. Android provides many classes out of the box to facilitate PKI - Read up on the following
classes:
• KeyPairGenerator -
https://developer.android.com/reference/java/security/KeyPairGenerator.html
• KeyPair - https://developer.android.com/reference/java/security/KeyPair.html
3. Create a ContentProvider that allows the user to request a public and private key pair using the
Rivest, Shamir, Aldeman (RSA) algorithm
4. When a request is made, the pubic and private keys will be returned in a cursor - one possibility
is to return them as strings
5. Your client activity (or activities) will do the following:
1. Allow the user to request a new key pair
2. Once received the key pair must be saved (but it does not have to be displayed to the user
explicitly – simply indicating that a pair has been generated is enough)
3. The user should have the ability to enter some text in a text field, and have the text encrypted
using the private key by clicking a button, and decrypted by clicking another button (feel free to
use other methods of invoking the encryption and decryption procedures, as long as the user has
control over the process)
Notes:
As you would expect, there are many examples of performing encryption and decryption on the
web. Some utilize Android’s build in framework, and others may utilize external libraries (example
Spongy Castle). You are encouraged to examine these solutions, but as always try to use these
examples to aid in understanding, and not simply as a source for copying code
