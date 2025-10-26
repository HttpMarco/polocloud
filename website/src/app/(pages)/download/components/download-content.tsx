'use client';
import { useEffect, useState } from 'react';
import Link from 'next/link';
import { 
  ArrowLeft, 
  Download, 
  Github,
  ExternalLink,
  Calendar,
  MessageCircle,
  AlertTriangle
} from 'lucide-react';

interface GitHubRelease {
  id: number;
  name: string;
  tag_name: string;
  body: string;
  published_at: string;
  html_url: string;
  assets: Array<{
    id: number;
    name: string;
    size: number;
    download_count: number;
    browser_download_url: string;
  }>;
  prerelease: boolean;
  draft: boolean;
}

interface Release {
  id: number;
  title: string;
  version: string;
  date: string;
  downloadUrl: string;
  githubUrl: string;
  isPrerelease: boolean;
  assets: Array<{
    name: string;
    size: string;
    downloadCount: number;
    downloadUrl: string;
  }>;
}

async function getGitHubReleases(): Promise<Release[]> {
  try {
    const cached = localStorage.getItem('github-releases-cache');
    const cacheTime = localStorage.getItem('github-releases-cache-time');

    if (cached && cacheTime && (Date.now() - parseInt(cacheTime)) < 1 * 60 * 1000) {
      return JSON.parse(cached);
    }

    const response = await fetch('https://api.github.com/repos/HttpMarco/polocloud/releases');
    if (!response.ok) {
      throw new Error('Failed to fetch releases');
    }

    const releases: GitHubRelease[] = await response.json();

    const processedReleases: Release[] = releases
      .filter(release => !release.draft)
      .map(release => ({
        id: release.id,
        title: release.name || release.tag_name,
        version: release.tag_name,
        date: release.published_at,
        downloadUrl: release.html_url,
        githubUrl: release.html_url,
        isPrerelease: release.prerelease,
        assets: release.assets.map(asset => ({
          name: asset.name,
          size: formatFileSize(asset.size),
          downloadCount: asset.download_count,
          downloadUrl: asset.browser_download_url
        }))
      }))
      .slice(0, 8);

    localStorage.setItem('github-releases-cache', JSON.stringify(processedReleases));
    localStorage.setItem('github-releases-cache-time', Date.now().toString());

    return processedReleases;
  } catch (error) {
    console.error('Error fetching GitHub releases:', error);
    return [];
  }
}

function formatFileSize(bytes: number): string {
  if (bytes === 0) return '0 Bytes';
  const k = 1024;
  const sizes = ['Bytes', 'KB', 'MB', 'GB'];
  const i = Math.floor(Math.log(bytes) / Math.log(k));
  return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i];
}

function cleanVersionText(version: string): string {
  return version.replace('-SNAPSHOT', '').replace('-SNAPSHOT', '');
}

export function DownloadContent() {
  const [releases, setReleases] = useState<Release[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  const _clearCacheAndReload = () => {
    localStorage.removeItem('github-releases-cache');
    localStorage.removeItem('github-releases-cache-time');
    fetchReleases();
  };

  const fetchReleases = async () => {
    setLoading(true);
    try {
      const fetchedReleases = await getGitHubReleases();
      setReleases(fetchedReleases);
      setError(null);
    } catch (err) {
      setError('Failed to load releases');
      console.error('Error loading releases:', err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchReleases();
  }, []);

  const sortedReleases = [...releases].sort((a, b) => 
    new Date(b.date).getTime() - new Date(a.date).getTime()
  );

  return (
    <div className="relative min-h-screen">
      <div className="absolute inset-0 bg-[linear-gradient(rgba(255,255,255,0.02)_1px,transparent_1px),linear-gradient(90deg,rgba(255,255,255,0.02)_1px,transparent_1px)] bg-[size:50px_50px] dark:bg-[linear-gradient(rgba(255,255,255,0.01)_1px,transparent_1px),linear-gradient(90deg,rgba(255,255,255,0.01)_1px,transparent_1px)]" />
      
      <div className="container mx-auto px-4 sm:px-6 py-8 sm:py-12 max-w-5xl relative z-10">
        <div className="mb-6 sm:mb-8">
          <Link 
            href="/"
            className="inline-flex items-center gap-2 px-3 sm:px-4 py-2 bg-card/50 hover:bg-card border border-border/50 rounded-lg text-sm font-medium transition-all duration-300 hover:scale-105 backdrop-blur-sm"
          >
            <ArrowLeft className="w-4 h-4" />
            <span className="hidden sm:inline">Back to Home</span>
            <span className="sm:hidden">Back</span>
          </Link>
        </div>

        <div className="text-center mb-12 sm:mb-16">
          <div className="flex items-center justify-center gap-3 mb-4 sm:mb-6">
            <div className="flex items-center justify-center w-8 h-8 sm:w-10 sm:h-10 bg-primary/10 text-primary rounded-full border border-primary/20">
              <Download className="w-4 h-4 sm:w-5 sm:h-5" />
            </div>
          </div>
          
          <h1 className="text-2xl sm:text-3xl md:text-4xl lg:text-5xl font-black bg-gradient-to-r from-foreground via-primary to-foreground bg-clip-text text-transparent mb-4 sm:mb-6 leading-tight">
            Download PoloCloud
          </h1>
          
          <p className="text-sm sm:text-base md:text-lg text-muted-foreground max-w-3xl mx-auto leading-relaxed px-4 sm:px-0">
            Get the latest version of PoloCloud and start managing your Minecraft servers with ease
          </p>
        </div>

        <div className="mb-12 sm:mb-16">
          {loading ? (
            <div className="text-center py-12 sm:py-16">
              <div className="animate-spin rounded-full h-10 w-10 sm:h-12 sm:w-12 border-b-2 border-primary mx-auto mb-3 sm:mb-4"></div>
              <p className="text-sm sm:text-base text-muted-foreground">Loading releases...</p>
            </div>
          ) : error ? (
            <div className="text-center py-12 sm:py-16">
              <Download className="w-12 h-12 sm:w-16 sm:h-16 text-muted-foreground mx-auto mb-3 sm:mb-4" />
              <h3 className="text-lg sm:text-xl font-semibold text-foreground mb-2">Failed to load releases</h3>
              <p className="text-sm sm:text-base text-muted-foreground mb-3 sm:mb-4">{error}</p>
              <a
                href="https://github.com/HttpMarco/polocloud/releases"
                target="_blank"
                rel="noopener noreferrer"
                className="inline-flex items-center gap-2 px-3 sm:px-4 py-2 bg-primary text-primary-foreground rounded-lg hover:bg-primary/90 transition-colors text-sm"
              >
                <Github className="w-4 h-4" />
                <span className="hidden sm:inline">View on GitHub</span>
                <span className="sm:hidden">GitHub</span>
              </a>
            </div>
          ) : releases.length === 0 ? (
            <div className="text-center py-12 sm:py-16">
              <Download className="w-12 h-12 sm:w-16 sm:h-16 text-muted-foreground mx-auto mb-3 sm:mb-4" />
              <h3 className="text-lg sm:text-xl font-semibold text-foreground mb-2">No releases available</h3>
              <p className="text-sm sm:text-base text-muted-foreground">Check back later for new releases.</p>
            </div>
          ) : (
            <div className="space-y-6 sm:space-y-8">
              {sortedReleases.length > 0 && (
                <div>
                  <div className="text-center mb-4 sm:mb-6">
                    <h2 className="text-xl sm:text-2xl font-bold text-foreground mb-2">Latest Release</h2>
                    <p className="text-sm sm:text-base text-muted-foreground">The most recent version of PoloCloud</p>
                  </div>
                  <div className="max-w-2xl mx-auto">
                    {(() => {
                      const latestRelease = sortedReleases[0];
                      return (
                        <article
                          key={latestRelease.id}
                          className="group bg-card/30 backdrop-blur-sm border border-border/50 rounded-xl p-4 sm:p-6 hover:bg-card/40 hover:border-border/70 transition-all duration-300 hover:scale-[1.02] relative overflow-hidden"
                        >
                          <div className="absolute inset-0 bg-gradient-to-br from-primary/5 via-transparent to-primary/5 opacity-0 group-hover:opacity-100 transition-opacity duration-300" />

                          <div className="relative z-10">
                            <div className="flex items-start justify-between mb-3 sm:mb-4">
                              <div className="flex items-center gap-2 sm:gap-3">
                                <Github className="w-5 h-5 sm:w-6 sm:h-6 text-primary" />
                                <div>
                                  <h3 className="text-base sm:text-lg font-bold text-foreground group-hover:text-primary transition-colors">
                                    {cleanVersionText(latestRelease.version)}
                                  </h3>
                                </div>
                              </div>
                            </div>

                            <div className="flex items-center gap-2 text-xs sm:text-sm text-muted-foreground mb-4 sm:mb-6">
                              <Calendar className="w-3 h-3 sm:w-4 sm:h-4" />
                              <span>{new Date(latestRelease.date).toLocaleDateString('en-US', {
                                year: 'numeric',
                                month: 'long',
                                day: 'numeric'
                              })}</span>
                            </div>

                            {latestRelease.assets.length > 0 && (
                              <div className="mb-4 sm:mb-6">
                                <div className="grid gap-2">
                                  {latestRelease.assets.slice(0, 3).map((asset) => (
                                    <div key={asset.name} className="flex items-center justify-between bg-muted/30 rounded-lg p-2 sm:p-3 hover:bg-muted/50 transition-colors">
                                      <div className="flex-1 min-w-0">
                                        <p className="text-xs sm:text-sm font-medium text-foreground truncate">{asset.name}</p>
                                        <p className="text-xs text-muted-foreground">{asset.size} • {asset.downloadCount} downloads</p>
                                      </div>
                                      <a
                                        href={asset.downloadUrl}
                                        target="_blank"
                                        rel="noopener noreferrer"
                                        className="ml-2 sm:ml-3 p-1.5 sm:p-2 text-primary hover:text-primary/80 hover:bg-primary/10 rounded-lg transition-all duration-200"
                                      >
                                        <Download className="w-3 h-3 sm:w-4 sm:h-4" />
                                      </a>
                                    </div>
                                  ))}
                                  {latestRelease.assets.length > 3 && (
                                    <div className="text-center py-2">
                                      <span className="text-xs text-muted-foreground">
                                        +{latestRelease.assets.length - 3} more files
                                      </span>
                                    </div>
                                  )}
                                </div>
                              </div>
                            )}

                            <div className="flex flex-col sm:flex-row gap-2 sm:gap-3">
                              <a
                                href={latestRelease.downloadUrl}
                                target="_blank"
                                rel="noopener noreferrer"
                                className="flex-1 inline-flex items-center justify-center gap-2 px-3 sm:px-4 py-2 sm:py-2.5 bg-primary text-primary-foreground text-sm font-medium rounded-lg hover:bg-primary/90 transition-all duration-200 hover:scale-105"
                              >
                                <Download className="w-3 h-3 sm:w-4 sm:h-4" />
                                <span className="hidden sm:inline">Download</span>
                                <span className="sm:hidden">Get</span>
                                <ExternalLink className="w-2.5 h-2.5 sm:w-3 sm:h-3" />
                              </a>
                              <a
                                href={latestRelease.githubUrl}
                                target="_blank"
                                rel="noopener noreferrer"
                                className="inline-flex items-center justify-center gap-2 px-3 sm:px-4 py-2 sm:py-2.5 bg-card/50 hover:bg-card border border-border/50 text-foreground text-sm font-medium rounded-lg transition-all duration-200 hover:scale-105 backdrop-blur-sm"
                              >
                                <Github className="w-3 h-3 sm:w-4 sm:h-4" />
                                <ExternalLink className="w-2.5 h-2.5 sm:w-3 sm:h-3" />
                              </a>
                            </div>
                          </div>
                        </article>
                      );
                    })()}
                  </div>
                </div>
              )}

              {sortedReleases.length > 1 && (
                <div>
                  <div className="text-center mb-4 sm:mb-6">
                    <h2 className="text-xl sm:text-2xl font-bold text-foreground mb-2">Previous Releases</h2>
                    <p className="text-sm sm:text-base text-muted-foreground">Previous versions of PoloCloud</p>
                  </div>

                  <div className="max-w-2xl mx-auto mb-6">
                    <div className="bg-amber-500/10 border border-amber-500/20 rounded-lg p-3 sm:p-4 text-center">
                      <div className="flex items-center justify-center gap-2 mb-2">
                        <AlertTriangle className="w-4 h-4 text-amber-500" />
                        <span className="text-sm font-medium text-amber-600 dark:text-amber-400">Older Version Warning</span>
                        <AlertTriangle className="w-4 h-4 text-amber-500" />
                      </div>
                      <p className="text-xs text-amber-600/80 dark:text-amber-400/80">
                        These are older versions and may contain security vulnerabilities or bugs. 
                        We recommend using the latest release for production environments.
                      </p>
                    </div>
                  </div>
                  
                  <div className="grid gap-4 sm:gap-6 md:grid-cols-2">
                    {sortedReleases.slice(1).map((release) => {
                      return (
                        <article
                          key={release.id}
                          className="group bg-card/30 backdrop-blur-sm border border-border/50 rounded-xl p-4 sm:p-6 hover:bg-card/40 hover:border-border/70 transition-all duration-300 hover:scale-[1.02] relative overflow-hidden"
                        >
                          <div className="absolute inset-0 bg-gradient-to-br from-primary/5 via-transparent to-primary/5 opacity-0 group-hover:opacity-100 transition-opacity duration-300" />

                          <div className="relative z-10">
                            <div className="flex items-start justify-between mb-3 sm:mb-4">
                              <div className="flex items-center gap-2 sm:gap-3">
                                <Github className="w-5 h-5 sm:w-6 sm:h-6 text-primary" />
                                <div>
                                  <h3 className="text-base sm:text-lg font-bold text-foreground group-hover:text-primary transition-colors">
                                    {cleanVersionText(release.version)}
                                  </h3>
                                </div>
                              </div>
                            </div>

                            <div className="flex items-center gap-2 text-xs sm:text-sm text-muted-foreground mb-4 sm:mb-6">
                              <Calendar className="w-3 h-3 sm:w-4 sm:h-4" />
                              <span>{new Date(release.date).toLocaleDateString('en-US', {
                                year: 'numeric',
                                month: 'long',
                                day: 'numeric'
                              })}</span>
                            </div>

                            {release.assets.length > 0 && (
                              <div className="mb-4 sm:mb-6">
                                <div className="grid gap-2">
                                  {release.assets.slice(0, 3).map((asset) => (
                                    <div key={asset.name} className="flex items-center justify-between bg-muted/30 rounded-lg p-2 sm:p-3 hover:bg-muted/50 transition-colors">
                                      <div className="flex-1 min-w-0">
                                        <p className="text-xs sm:text-sm font-medium text-foreground truncate">{asset.name}</p>
                                        <p className="text-xs text-muted-foreground">{asset.size} • {asset.downloadCount} downloads</p>
                                      </div>
                                      <a
                                        href={asset.downloadUrl}
                                        target="_blank"
                                        rel="noopener noreferrer"
                                        className="ml-2 sm:ml-3 p-1.5 sm:p-2 text-primary hover:text-primary/80 hover:bg-primary/10 rounded-lg transition-all duration-200"
                                      >
                                        <Download className="w-3 h-3 sm:w-4 sm:h-4" />
                                      </a>
                                    </div>
                                  ))}
                                  {release.assets.length > 3 && (
                                    <div className="text-center py-2">
                                      <span className="text-xs text-muted-foreground">
                                        +{release.assets.length - 3} more files
                                      </span>
                                    </div>
                                  )}
                                </div>
                              </div>
                            )}

                            <div className="flex flex-col sm:flex-row gap-2 sm:gap-3">
                              <a
                                href={release.downloadUrl}
                                target="_blank"
                                rel="noopener noreferrer"
                                className="flex-1 inline-flex items-center justify-center gap-2 px-3 sm:px-4 py-2 sm:py-2.5 bg-primary text-primary-foreground text-sm font-medium rounded-lg hover:bg-primary/90 transition-all duration-200 hover:scale-105"
                              >
                                <Download className="w-3 h-3 sm:w-4 sm:h-4" />
                                <span className="hidden sm:inline">Download</span>
                                <span className="sm:hidden">Get</span>
                                <ExternalLink className="w-2.5 h-2.5 sm:w-3 sm:h-3" />
                              </a>
                              <a
                                href={release.githubUrl}
                                target="_blank"
                                rel="noopener noreferrer"
                                className="inline-flex items-center justify-center gap-2 px-3 sm:px-4 py-2 sm:py-2.5 bg-card/50 hover:bg-card border border-border/50 text-foreground text-sm font-medium rounded-lg transition-all duration-200 hover:scale-105 backdrop-blur-sm"
                              >
                                <Github className="w-3 h-3 sm:w-4 sm:h-4" />
                                <ExternalLink className="w-2.5 h-2.5 sm:w-3 sm:h-3" />
                              </a>
                            </div>
                          </div>
                        </article>
                      );
                    })}
                  </div>
                </div>
              )}
            </div>
          )}
        </div>

        <div className="mt-12 sm:mt-16 bg-gradient-to-r from-primary/10 via-primary/5 to-primary/10 border border-primary/20 rounded-xl p-4 sm:p-6 text-center">
          <MessageCircle className="w-6 h-6 sm:w-8 sm:h-8 text-primary mx-auto mb-2 sm:mb-3" />
          <h3 className="text-lg sm:text-xl font-bold text-foreground dark:text-white mb-2">
            Join Our Community
          </h3>
          <p className="text-xs sm:text-sm text-muted-foreground mb-3 sm:mb-4 max-w-2xl mx-auto px-2 sm:px-0">
            Get notified about new releases, discuss features, and connect with other PoloCloud users. 
            Never miss an update again!
          </p>
          <a
            href="https://discord.polocloud.de/"
            target="_blank"
            rel="noopener noreferrer"
            className="inline-flex items-center gap-2 bg-primary text-primary-foreground font-medium px-3 sm:px-4 py-2 rounded-lg hover:bg-primary/90 transition-colors text-sm"
          >
            <MessageCircle className="w-3 h-3 sm:w-4 sm:h-4" />
            <span className="hidden sm:inline">Join Discord Server</span>
            <span className="sm:hidden">Join Discord</span>
          </a>
        </div>
      </div>
    </div>
  );
} 