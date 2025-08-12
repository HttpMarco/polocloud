"use client";

import { Search, Check, X, Clock, AlertCircle, Edit3 } from 'lucide-react';
import { Feedback } from './types';

interface FeedbackTabProps {
  feedbacks: Feedback[];
  query: string;
  setQuery: (query: string) => void;
  ratingFilter: number | null;
  setRatingFilter: (rating: number | null) => void;
  statusFilter: 'ALL' | 'PENDING' | 'APPROVED' | 'REJECTED';
  setStatusFilter: (status: 'ALL' | 'PENDING' | 'APPROVED' | 'REJECTED') => void;
  filteredFeedbacks: Feedback[];
  feedbackStatusCounts: Record<string, number>;
  onApprove: (feedbackId: string) => void;
  onReject: (feedbackId: string) => void;
  onRefresh: () => void;
}

export function FeedbackTab({
  feedbacks,
  query,
  setQuery,
  ratingFilter,
  setRatingFilter,
  statusFilter,
  setStatusFilter,
  filteredFeedbacks,
  feedbackStatusCounts,
  onApprove,
  onReject,
  onRefresh
}: FeedbackTabProps) {
  return (
    <div>
      <div className="grid md:grid-cols-3 gap-4 mb-6">
        <div className="flex items-center gap-2 rounded-lg border border-border/50 bg-background/50 px-3 py-2">
          <Search className="w-4 h-4 text-muted-foreground" />
          <input
            value={query}
            onChange={(e) => setQuery(e.target.value)}
            placeholder="Search by user or description"
            className="w-full bg-transparent outline-none text-foreground placeholder:text-muted-foreground"
          />
        </div>
        <div className="flex gap-2 overflow-x-auto">
          {[1, 2, 3, 4, 5].map((rating) => (
            <button
              key={rating}
              onClick={() => setRatingFilter(ratingFilter === rating ? null : rating)}
              className={`flex items-center gap-1 px-3 py-2 rounded-lg border transition-all duration-200 ${
                ratingFilter === rating
                  ? 'border-primary bg-primary/10 text-primary'
                  : 'border-border/50 bg-background/50 text-muted-foreground hover:text-foreground hover:bg-background/70'
              }`}
            >
              {'★'.repeat(rating)}
            </button>
          ))}
        </div>
        <div className="flex gap-2 overflow-x-auto">
          {(['ALL', 'PENDING', 'APPROVED', 'REJECTED'] as const).map((status) => (
            <button
              key={status}
              onClick={() => setStatusFilter(status)}
              className={`flex items-center gap-1 px-3 py-2 rounded-lg border transition-all duration-200 ${
                statusFilter === status
                  ? 'border-primary bg-primary/10 text-primary'
                  : 'border-border/50 bg-background/50 text-muted-foreground hover:text-foreground hover:bg-background/70'
              }`}
            >
              {status === 'PENDING' && <Clock className="w-3 h-3" />}
              {status === 'APPROVED' && <Check className="w-3 h-3" />}
              {status === 'REJECTED' && <X className="w-3 h-3" />}
              {status === 'ALL' && <Edit3 className="w-3 h-3" />}
              {status} {status !== 'ALL' && `(${feedbackStatusCounts[status] || 0})`}
            </button>
          ))}
        </div>
      </div>

      <div className="grid md:grid-cols-4 gap-4 mb-6">
        <div className="p-4 bg-gradient-to-br from-blue-500/5 via-transparent to-blue-500/5 border border-blue-500/20 rounded-xl">
          <div className="text-2xl font-bold text-blue-600">{feedbacks.length}</div>
          <div className="text-sm text-muted-foreground">Total Feedbacks</div>
        </div>
        <div className="p-4 bg-gradient-to-br from-yellow-500/5 via-transparent to-yellow-500/5 border border-yellow-500/20 rounded-xl">
          <div className="text-2xl font-bold text-yellow-600">{feedbackStatusCounts['PENDING'] || 0}</div>
          <div className="text-sm text-muted-foreground">Pending</div>
        </div>
        <div className="p-4 bg-gradient-to-br from-green-500/5 via-transparent to-green-500/5 border border-green-500/20 rounded-xl">
          <div className="text-2xl font-bold text-green-600">{feedbackStatusCounts['APPROVED'] || 0}</div>
          <div className="text-sm text-muted-foreground">Approved</div>
        </div>
        <div className="p-4 bg-gradient-to-br from-red-500/5 via-transparent to-red-500/5 border border-red-500/20 rounded-xl">
          <div className="text-2xl font-bold text-red-600">{feedbackStatusCounts['REJECTED'] || 0}</div>
          <div className="text-sm text-muted-foreground">Rejected</div>
        </div>
      </div>

      <div className="space-y-4">
        {filteredFeedbacks.length === 0 ? (
          <div className="text-center py-12 text-muted-foreground">
            {query || ratingFilter || statusFilter !== 'ALL' ? 'No feedbacks match your filters.' : 'No feedbacks yet.'}
          </div>
        ) : (
          filteredFeedbacks.map((feedback) => (
            <div
              key={feedback.id}
              className={`p-6 rounded-xl border transition-all duration-300 ${
                feedback.status === 'APPROVED'
                  ? 'bg-gradient-to-br from-green-500/5 via-transparent to-green-500/5 border-green-500/20'
                  : feedback.status === 'REJECTED'
                  ? 'bg-gradient-to-br from-red-500/5 via-transparent to-red-500/5 border-red-500/20'
                  : 'bg-gradient-to-br from-yellow-500/5 via-transparent to-yellow-500/5 border-yellow-500/20'
              }`}
            >
              <div className="flex items-start justify-between mb-4">
                <div className="flex items-center gap-3">
                  <img
                    src={feedback.avatar}
                    alt={feedback.username}
                    className="w-10 h-10 rounded-full border border-border/50"
                  />
                  <div>
                    <div className="font-medium text-foreground">{feedback.username}</div>
                    <div className="text-sm text-muted-foreground">
                      {new Date(feedback.createdAt).toLocaleDateString('de-DE')} • {new Date(feedback.createdAt).toLocaleTimeString('de-DE', { hour: '2-digit', minute: '2-digit' })}
                    </div>
                  </div>
                </div>
                <div className="flex items-center gap-2">
                  <div className="text-yellow-500">{'★'.repeat(feedback.rating)}</div>
                  <div className={`px-2 py-1 rounded-full text-xs font-medium ${
                    feedback.status === 'APPROVED'
                      ? 'bg-green-500/10 text-green-600'
                      : feedback.status === 'REJECTED'
                      ? 'bg-red-500/10 text-red-600'
                      : 'bg-yellow-500/10 text-yellow-600'
                  }`}>
                    {feedback.status}
                  </div>
                </div>
              </div>
              
              <div className="mb-4 text-foreground">{feedback.description}</div>
              
              {feedback.status === 'PENDING' && (
                <div className="flex gap-2">
                  <button
                    onClick={() => onApprove(feedback.id)}
                    className="inline-flex items-center gap-2 px-3 py-2 bg-green-600 text-white rounded-lg hover:bg-green-700 transition-all duration-300"
                  >
                    <Check className="w-4 h-4" />
                    Approve
                  </button>
                  <button
                    onClick={() => onReject(feedback.id)}
                    className="inline-flex items-center gap-2 px-3 py-2 bg-red-600 text-white rounded-lg hover:bg-red-700 transition-all duration-300"
                  >
                    <X className="w-4 h-4" />
                    Reject
                  </button>
                </div>
              )}
              
              {feedback.status === 'APPROVED' && feedback.approvedBy && (
                <div className="text-sm text-muted-foreground">
                  Approved by {feedback.approvedBy} on {feedback.approvedAt ? new Date(feedback.approvedAt).toLocaleDateString('de-DE') : 'unknown date'}
                </div>
              )}
              
                             {feedback.status === 'REJECTED' && feedback.rejectedBy && (
                 <div className="text-sm text-muted-foreground">
                   Rejected by {feedback.rejectedBy} on {feedback.rejectedAt ? new Date(feedback.rejectedAt).toLocaleDateString('de-DE') : 'unknown date'}
                 </div>
               )}
            </div>
          ))
        )}
      </div>
    </div>
  );
}
