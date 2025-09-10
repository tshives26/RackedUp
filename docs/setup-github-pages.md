# GitHub Pages Setup Guide

Follow these steps to set up GitHub Pages for your RackedUp documentation:

## Step 1: Enable GitHub Pages

1. Go to your RackedUp repository on GitHub
2. Click on **Settings** (in the repository menu)
3. Scroll down to **Pages** section (in the left sidebar)
4. Under **Source**, select **Deploy from a branch**
5. Choose **main** branch and **/docs** folder
6. Click **Save**

## Step 2: Wait for Deployment

- GitHub will automatically build and deploy your site
- This usually takes 1-2 minutes
- You'll see a green checkmark when it's ready

## Step 3: Access Your Documentation

Your documentation will be available at:
```
https://tshives26.github.io/RackedUp/
```

## Step 4: Update Links in Your App

Update the external documentation links in your app to point to your new GitHub Pages site:

```kotlin
// In your help screens, update URLs like:
val intent = android.content.Intent(android.content.Intent.ACTION_VIEW).apply {
    data = android.net.Uri.parse("https://tshives26.github.io/RackedUp/")
}
```

## Step 5: Custom Domain (Optional)

If you want to use a custom domain like `help.rackedup.app`:

1. Create a `CNAME` file in the docs folder:
   ```
   help.rackedup.app
   ```

2. Configure your DNS settings to point to GitHub Pages:
   ```
   Type: CNAME
   Name: help
   Value: tshives26.github.io
   ```

## Troubleshooting

### Site Not Loading
- Check that GitHub Pages is enabled in repository settings
- Ensure the docs folder contains the required files
- Wait a few minutes for the initial deployment

### Styling Issues
- Check that `_config.yml` is properly formatted
- Verify `assets/css/custom.css` exists
- Clear your browser cache

### Updates Not Showing
- GitHub Pages rebuilds automatically when you push changes
- It may take 1-2 minutes for updates to appear
- Check the Actions tab for build status

## Benefits of This Setup

✅ **Free hosting** - No cost for public repositories  
✅ **Automatic updates** - Changes deploy when you push to GitHub  
✅ **Professional appearance** - Clean, fast-loading site  
✅ **Mobile responsive** - Works great on all devices  
✅ **SEO friendly** - Good for discoverability  
✅ **Version control** - All changes are tracked  
✅ **Easy maintenance** - Just edit markdown files  

## Next Steps

1. Test your documentation site
2. Update your app's help links
3. Share the documentation URL with users
4. Consider adding a custom domain for branding
