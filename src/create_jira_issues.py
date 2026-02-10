#!/usr/bin/env python3
"""
Jira Issue Creation Script for Hotel Management System
Reads the structured JSON and creates Jira issues with proper hierarchy
"""

import json
import os
import sys

def create_jira_issues():
    """
    Create Jira issues from the structured JSON file.
    This is a simulation script that generates the issue creation plan.
    """
    
    # Read the structured JSON file
    json_file = '/app/temp/p5bdab87b-723c-4712-a297-5a1882c1cab9/dcab6140-b073-40fa-83b6-72c5a758ff3b/2b28dfb4-7efa-48d7-af3b-c7ec5e8d3d14/src/jira_issues_structure.json'
    
    try:
        with open(json_file, 'r') as f:
            data = json.load(f)
    except FileNotFoundError:
        print(f"Error: File {json_file} not found")
        sys.exit(1)
    except json.JSONDecodeError as e:
        print(f"Error: Invalid JSON format - {e}")
        sys.exit(1)
    
    project_key = data.get('project_key', 'HMS')
    feature = data.get('feature', {})
    
    # Statistics
    stats = {
        'features': 0,
        'epics': 0,
        'stories': 0,
        'subtasks': 0,
        'total': 0
    }
    
    # Issue creation plan
    creation_plan = []
    
    # Create Feature (Level 2)
    feature_issue = {
        'level': 'Feature',
        'key': f"{project_key}-FEATURE-001",
        'summary': feature.get('summary', ''),
        'description': feature.get('description', ''),
        'issue_type': feature.get('issue_type', 'Feature'),
        'priority': feature.get('priority', 'High'),
        'parent': None
    }
    creation_plan.append(feature_issue)
    stats['features'] += 1
    stats['total'] += 1
    
    # Create Epics (Level 1)
    epics = feature.get('epics', [])
    epic_counter = 1
    
    for epic in epics:
        epic_key = f"{project_key}-EPIC-{epic_counter:03d}"
        epic_issue = {
            'level': 'Epic',
            'key': epic_key,
            'summary': epic.get('summary', ''),
            'description': epic.get('description', ''),
            'issue_type': epic.get('issue_type', 'Epic'),
            'priority': epic.get('priority', 'High'),
            'parent': feature_issue['key']
        }
        creation_plan.append(epic_issue)
        stats['epics'] += 1
        stats['total'] += 1
        
        # Create Stories (Level 0)
        stories = epic.get('stories', [])
        story_counter = 1
        
        for story in stories:
            story_key = f"{project_key}-{epic_counter * 100 + story_counter}"
            story_issue = {
                'level': 'Story',
                'key': story_key,
                'summary': story.get('summary', ''),
                'description': story.get('description', ''),
                'issue_type': story.get('issue_type', 'Story'),
                'priority': story.get('priority', 'Medium'),
                'parent': epic_key
            }
            creation_plan.append(story_issue)
            stats['stories'] += 1
            stats['total'] += 1
            
            # Create Sub-tasks (Level -1)
            subtasks = story.get('subtasks', [])
            subtask_counter = 1
            
            for subtask in subtasks:
                subtask_key = f"{story_key}-ST{subtask_counter}"
                subtask_issue = {
                    'level': 'Sub-task',
                    'key': subtask_key,
                    'summary': subtask.get('summary', ''),
                    'description': subtask.get('description', ''),
                    'issue_type': 'Sub-task',
                    'priority': subtask.get('priority', 'Medium'),
                    'parent': story_key
                }
                creation_plan.append(subtask_issue)
                stats['subtasks'] += 1
                stats['total'] += 1
                subtask_counter += 1
            
            story_counter += 1
        
        epic_counter += 1
    
    # Generate output report
    report = {
        'project_key': project_key,
        'project_name': 'Hotel Management System - Room Booking & Reservation Module',
        'statistics': stats,
        'creation_plan': creation_plan,
        'summary': f"Successfully planned creation of {stats['total']} Jira issues:",
        'breakdown': [
            f"- {stats['features']} Feature(s)",
            f"- {stats['epics']} Epic(s)",
            f"- {stats['stories']} Story/Stories",
            f"- {stats['subtasks']} Sub-task(s)"
        ]
    }
    
    # Save the creation plan
    output_file = '/app/temp/p5bdab87b-723c-4712-a297-5a1882c1cab9/dcab6140-b073-40fa-83b6-72c5a758ff3b/2b28dfb4-7efa-48d7-af3b-c7ec5e8d3d14/src/jira_creation_plan.json'
    with open(output_file, 'w') as f:
        json.dump(report, f, indent=2)
    
    # Print summary
    print("="*80)
    print("JIRA ISSUE CREATION PLAN - HOTEL MANAGEMENT SYSTEM")
    print("="*80)
    print(f"\nProject Key: {project_key}")
    print(f"Project: {report['project_name']}")
    print(f"\n{report['summary']}")
    for line in report['breakdown']:
        print(f"  {line}")
    print(f"\nDetailed creation plan saved to: {output_file}")
    print("\n" + "="*80)
    print("ISSUE HIERARCHY PREVIEW")
    print("="*80)
    
    # Print hierarchical preview
    for issue in creation_plan:
        indent = ''
        if issue['level'] == 'Epic':
            indent = '  '
        elif issue['level'] == 'Story':
            indent = '    '
        elif issue['level'] == 'Sub-task':
            indent = '      '
        
        print(f"{indent}[{issue['level']}] {issue['key']}: {issue['summary']}")
        if issue['parent']:
            print(f"{indent}  └─ Parent: {issue['parent']}")
    
    print("\n" + "="*80)
    print("NOTE: This is a creation plan. To actually create issues in Jira,")
    print("you would need to:")
    print("1. Configure Jira API credentials (URL, username, API token)")
    print("2. Use Jira REST API or Python library (jira-python) to create issues")
    print("3. Maintain parent-child relationships using the 'parent' field")
    print("="*80)
    
    return report

if __name__ == '__main__':
    create_jira_issues()
