//invalidate JWT Token via simply removing it from local storage of browser and transfer user to index.html


localStorage.clear();
document.cookie = `JWTToken=TokenNotSet; path=/`;
console.log("JWT Token has been cleared");
console.log("redirecting to main page");
setTimeout(() => {
    window.location.href = "http://localhost:8080/"
}, 1000);
